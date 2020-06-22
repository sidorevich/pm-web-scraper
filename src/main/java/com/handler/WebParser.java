package com.handler;

import com.data.model.BetInfo;
import com.data.model.CoeffInfo;
import com.data.model.SportLobby;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Node;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static com.data.model.Const.*;
import static org.apache.commons.lang3.StringUtils.*;

public class WebParser {

    private static final Logger logger = Logger.getLogger(WebParser.class.getName());
    private final WebClient client = new WebClient(BrowserVersion.BEST_SUPPORTED);

    private List<SportLobby> getListOfSportLobby() {

        try {
            HtmlPage page = client.getPage(SEARCH_URL);

            List<HtmlAnchor> sportLobbyMenuLinks = page.getByXPath(XPATH_NAVIGATIONAL_MENU);

            if (sportLobbyMenuLinks == null || sportLobbyMenuLinks.isEmpty()) {
                return null;
            }

            return sportLobbyMenuLinks.stream().map(this::fillSportLobbyObject).filter(Objects::nonNull).collect(Collectors.toList());

        } catch (Exception e) {
            logger.log(Level.SEVERE, ERROR_MSG, e.getMessage());
            e.printStackTrace();
        }

        return Collections.emptyList();
    }

    private SportLobby fillSportLobbyObject(HtmlAnchor htmlAnchor) {

        if (htmlAnchor == null) {
            return null;
        }

        SportLobby sportLobby = new SportLobby();

        if (isNotEmpty(htmlAnchor.getHrefAttribute())) {
            sportLobby.setLink(SEARCH_URL + htmlAnchor.getHrefAttribute());
        }

        if (isNotEmpty(htmlAnchor.asText())) {
            sportLobby.setName(htmlAnchor.asText());
        }

        Node hdAttribute = htmlAnchor.getAttributes().getNamedItem(HD_ATTRIBUTE);
        if (hdAttribute != null && isNotEmpty(hdAttribute.getNodeValue())) {
            sportLobby.setTableId(hdAttribute.getNodeValue());
        } else {
            logger.warning(String.format("Can not find table ID for :%s!", isNotEmpty(htmlAnchor.getHrefAttribute()) ? htmlAnchor.getHrefAttribute() : ""));
            return null;
        }

        //Получение наименование вида спорта
        DomNode parentNode = htmlAnchor.getParentNode();
        if (parentNode != null
                && parentNode.getParentNode() != null
                && parentNode.getParentNode().getParentNode() != null
                && parentNode.getParentNode().getParentNode().getFirstChild() != null
                && isNotEmpty(parentNode.getParentNode().getParentNode().getFirstChild().asText())) {
            sportLobby.setSportName(parentNode.getParentNode().getParentNode().getFirstChild().asText());
        }

        return sportLobby;

    }

    public void runWebSiteParsing() {
        logger.info("Starting scraping web site");
        long t = System.currentTimeMillis();

        client.getOptions().setCssEnabled(false);
        client.getOptions().setJavaScriptEnabled(false);

        List<SportLobby> sportLobbies = getListOfSportLobby();

        if (sportLobbies == null || sportLobbies.isEmpty()) {
            return;
        }

        List<SportLobby> betInfoData;

        betInfoData = sportLobbies
                .parallelStream()
                .unordered()
                .map(this::scrapWebPage)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        if (betInfoData.isEmpty()) {
            logger.info(String.format("Can not get bets information for %s lobbies", sportLobbies.size()));
            return;
        }

        HtmlGenerator.writeToFileReceivedData(betInfoData);
        logger.info(String.format("Completed total scraping for:%s seconds", ((System.currentTimeMillis() - t) / 1000)));
        client.close();
    }

    private List<HtmlTableBody> getBetInfoTableBodies(HtmlPage contentPage, String tableId) {
        List<DomElement> contentTables = contentPage.getElementsByTagName(TABLE_TAG_VALUE);

        DomElement tableDomeElement = contentTables.stream().filter(Objects::nonNull).filter(domElement -> isNotEmpty(domElement.getAttribute(ID_ATTRIBUTE))
                && domElement.getAttribute(ID_ATTRIBUTE).contains(tableId))
                .findFirst().orElse(null);

        HtmlTable htmlTableContent = tableDomeElement instanceof HtmlTable ? (HtmlTable) tableDomeElement : null;


        if (htmlTableContent != null && htmlTableContent.getBodies() != null && htmlTableContent.getBodies().size() == 1) {

            List<HtmlTableBody> collectHtmlTableBodies = new ArrayList<>();

            List<HtmlTableCell> tableCells = htmlTableContent.getRows().stream()
                    .filter(htmlTableRow -> htmlTableRow.getCells() != null && !htmlTableRow.getCells().isEmpty())
                    .map(HtmlTableRow::getCells)
                    .flatMap(Collection::stream)
                    .collect(Collectors.toList());

            for (HtmlTableCell tableCell : tableCells) {
                collectHtmlTableBodies.addAll(tableCell.getChildNodes().stream()
                        .filter(domNode -> domNode instanceof HtmlTable)
                        .map(domNode -> ((HtmlTable) domNode).getBodies()).filter(Objects::nonNull)
                        .flatMap(Collection::stream)
                        .filter(this::isNotNecessaryBodies).collect(Collectors.toList()));
            }
            return collectHtmlTableBodies;
        } else {
            if (htmlTableContent != null && htmlTableContent.getBodies() != null) {
                return htmlTableContent.getBodies().stream().filter(this::isNotNecessaryBodies).collect(Collectors.toList());
            }
        }
        return Collections.emptyList();
    }

    private boolean isNotNecessaryBodies(HtmlTableBody htmlTable) {
        return (!htmlTable.getAttribute(STYLE_ATTRIBUTE).contains(NON_DISPLAY_ATTRIBUTE))
                && (htmlTable.getAttributes() != null && htmlTable.getAttributes().getNamedItem(ID_ATTRIBUTE) == null)
                && (!htmlTable.getAttribute(CLASS_ATTRIBUTE).contains(SPACE_ATTRIBUTE));
    }

    private boolean isContainsCellValue(HtmlTableBody tableBody, String value) {
        return !tableBody.getRows().isEmpty()
                && tableBody.getRows().stream().map(HtmlTableRow::getCells).flatMap(Collection::stream).anyMatch(htmlTableCell -> htmlTableCell.asText().contains(value));

    }

    private Integer getCellIndex(List<HtmlTableCell> tableCells, String... cellTitle) {

        if (tableCells == null || tableCells.isEmpty()) {
            return null;
        }

        for (int i = 0; i < tableCells.size(); i++) {
            for (String item : cellTitle) {
                if (StringUtils.equalsIgnoreCase(tableCells.get(i).asText(), item)) {
                    return i;
                }
            }

        }
        return null;
    }

    private List<HtmlTableCell> getCellsFromBody(HtmlTableBody tableBody) {
        return tableBody.getRows().stream()
                .map(HtmlTableRow::getCells)
                .flatMap(Collection::stream)
                .filter(Objects::nonNull).collect(Collectors.toList());
    }

    private SportLobby scrapWebPage(SportLobby sportLobby) {

        if (sportLobby == null || isEmpty(sportLobby.getTableId()) || isBlank(sportLobby.getTableId())) {
            logger.info("Can not get page, sportLobby or table empty");
            return null;
        }

        long t = System.currentTimeMillis();

        HtmlPage contentPage = null;

        StringBuilder sb = new StringBuilder(SEARCH_URL);
        sb.append(HD_CONTENT_URL);
        sb.append(sportLobby.getTableId());
        sb.append(HS_PARAM_URL);

        try {
            contentPage = client.getPage(sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
            logger.log(Level.SEVERE, e.getMessage());
        }

        if (contentPage == null) {
            logger.info(String.format("Can not get cont of page ID for :%s!", sb.toString()));
            return null;
        }

        List<HtmlTableBody> betInfoTableBodies = getBetInfoTableBodies(contentPage, sportLobby.getTableId());

        List<HtmlTableBody> headTableBodies = betInfoTableBodies.stream()
                .filter(tableBody -> isContainsCellValue(tableBody, P1_TITLE) || isContainsCellValue(tableBody, P2_TITLE)).collect(Collectors.toList());

        if (headTableBodies.isEmpty()) {
            logger.info(String.format("Can not find table with coefficient cells :for link %s ,Event name:%s", sb.toString(), sportLobby.getName()));
            return null;
        }

        betInfoTableBodies.removeAll(headTableBodies);

        HtmlTableBody mainTableBody = headTableBodies.stream().findAny().get();

        List<HtmlTableCell> headerCells = getCellsFromBody(mainTableBody);

        Map<String, Integer> indexMap = new HashMap<>(5);
        indexMap.put(DATE_TITLE, getCellIndex(headerCells, DATE_TITLE));
        indexMap.put(EVENT_TITLE, getCellIndex(headerCells, EVENT_TITLE));
        indexMap.put(P1_TITLE, getCellIndex(headerCells, P1_TITLE));
        indexMap.put(X_TITLE, getCellIndex(headerCells, X_TITLE, X_TITLE_LAT));
        indexMap.put(P2_TITLE, getCellIndex(headerCells, P2_TITLE));

        List<BetInfo> infoList = new ArrayList<>();

        try {
            for (HtmlTableBody betInfoBody : betInfoTableBodies) {

                List<HtmlTableCell> betInfoCells = getCellsFromBody(betInfoBody);

                List<String> betInfoValues;

                if (betInfoCells.size() != headerCells.size()) {
                    betInfoValues = getDividedColSpanValues(betInfoCells);
                } else {
                    betInfoValues = betInfoCells.stream().map(DomNode::asText).collect(Collectors.toList());
                }

                if (betInfoValues.isEmpty()) {
                    logger.info(String.format("Can not find table cells values for:%s", sb.toString()));
                    continue;
                }

                BetInfo betInfo = fillBetInfo(indexMap, betInfoValues);

                if (betInfo == null) {
                    logger.info(String.format("Can not find coefficients values for:%s", sb.toString()));
                    continue;
                }

                infoList.add(betInfo);
            }
        } catch (ParseException e) {
            logger.log(Level.SEVERE, String.format("Can not parse date for:%s details:%s", sb.toString(), e.getMessage()));
            e.getMessage();
        } catch (Exception e) {
            logger.log(Level.SEVERE, ERROR_MSG, e.getMessage());
            e.getMessage();
        }

        sportLobby.setBetInfoList(infoList);

        logger.info(String.format("Completed page scraping for:%s : %s seconds", sb.toString(), ((System.currentTimeMillis() - t) / 1000)));

        return sportLobby;

    }

    private BetInfo fillBetInfo(Map<String, Integer> indexMap, List<String> cellsData) throws ParseException {
        BetInfo betInfo = new BetInfo();
        Map<String, CoeffInfo> coefficientsInfo = new HashMap<>();
        if (indexMap.get(DATE_TITLE) != null) {
            String dateValue = cellsData.get(indexMap.get(DATE_TITLE));
            if (isNotEmpty(dateValue)) {
                String dateWithYearValue = LocalDate.now().getYear() + "-" + cellsData.get(indexMap.get(DATE_TITLE))
                        .replace("/", "-")
                        .replace("\r\n", " ");
                betInfo.setDate(new SimpleDateFormat(DATE_FORMAT_PATTERN).parse(dateWithYearValue).getTime());
            }
        }

        if (indexMap.get(EVENT_TITLE) != null) {
            String teamsValue = cellsData.get(indexMap.get(EVENT_TITLE));
            if (isNotEmpty(teamsValue) && teamsValue.contains("\r\n")) {
                String[] teamsArray = teamsValue.split("\r\n");
                if (teamsArray.length == 2) {
                    betInfo.setHomeTeam(teamsArray[0]);
                    betInfo.setVisitTeam(teamsArray[1]);
                }
            }

        }
        if (indexMap.get(P1_TITLE) != null) {
            CoeffInfo coeffInfo = new CoeffInfo(P1_TITLE);
            String winnerHomeTeamVal = cellsData.get(indexMap.get(P1_TITLE));
            if (isNotEmpty(winnerHomeTeamVal) && StringUtils.isNotBlank(winnerHomeTeamVal)) {
                coeffInfo.setValue(Double.valueOf(winnerHomeTeamVal));
            }
            coefficientsInfo.put(P1_TITLE, coeffInfo);
        }
        if (indexMap.get(X_TITLE) != null) {
            CoeffInfo coeffInfo = new CoeffInfo(X_TITLE);
            String drawValue = cellsData.get(indexMap.get(X_TITLE));
            if (isNotEmpty(drawValue) && StringUtils.isNotBlank(drawValue)) {
                coeffInfo.setValue(Double.valueOf(drawValue));
            }
            coefficientsInfo.put(X_TITLE, coeffInfo);
        }
        if (indexMap.get(P2_TITLE) != null) {
            CoeffInfo coeffInfo = new CoeffInfo(P2_TITLE);
            String winnerVisitTeamVal = cellsData.get(indexMap.get(P2_TITLE));
            if (isNotEmpty(winnerVisitTeamVal) && StringUtils.isNotBlank(winnerVisitTeamVal)) {
                coeffInfo.setValue(Double.valueOf(winnerVisitTeamVal));
            }
            coefficientsInfo.put(P2_TITLE, coeffInfo);
        }

        boolean isExistCoeffVal = coefficientsInfo.values().stream().anyMatch(item -> item != null && item.getValue() != null);

        if (!isExistCoeffVal) {
            return null;
        }

        betInfo.setCoefficients(coefficientsInfo);

        return betInfo;
    }

    private List<String> getDividedColSpanValues(List<HtmlTableCell> tableCells) {
        List<String> values = new ArrayList<>();
        for (HtmlTableCell item : tableCells) {
            if (item.getColumnSpan() > 1) {
                for (int i = 0; i < item.getColumnSpan(); i++) {
                    values.add("");
                }
            } else {
                values.add(item.asText());
            }
        }
        return values;
    }

}
