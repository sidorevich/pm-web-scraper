package com.handler;

import com.data.model.BetInfo;
import com.data.model.CoeffInfo;
import com.data.model.SportLobby;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Node;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static com.data.model.Const.*;

public class WebParser {

    private static final Logger logger = Logger.getLogger(WebParser.class.getName());
    //TODO
    WebClient client = new WebClient(BrowserVersion.BEST_SUPPORTED);

    //TODO
    private List<SportLobby> getListOfSportLobby(String menuXPath, String searchUrl) {
        //TODO
        client.getOptions().setCssEnabled(false);
        client.getOptions().setJavaScriptEnabled(false);

        try {
            HtmlPage page = client.getPage(searchUrl);

            List<HtmlAnchor> sportLobbyMenuLinks = page.getByXPath(menuXPath);
            //TODO
            if (sportLobbyMenuLinks == null || sportLobbyMenuLinks.isEmpty()) {
                return null;
            }

            return sportLobbyMenuLinks.stream().map(this::fillSportLobbyObject).filter(Objects::nonNull).collect(Collectors.toList());
            //TODO
        } catch (IOException | NullPointerException e) {
            logger.log(Level.WARNING, e.getMessage());
            e.printStackTrace();
        }

        return Collections.emptyList();
    }

    private SportLobby fillSportLobbyObject(HtmlAnchor htmlAnchor) {

        if (htmlAnchor == null) {
            return null;
        }

        SportLobby sportLobby = new SportLobby();

        if (StringUtils.isNotEmpty(htmlAnchor.getHrefAttribute())) {
            sportLobby.setLink(htmlAnchor.getHrefAttribute());
        }

        if (StringUtils.isNotEmpty(htmlAnchor.asText())) {
            sportLobby.setName(htmlAnchor.asText());
        }

        //TODO
        Node hdAttribute = htmlAnchor.getAttributes().getNamedItem(HD_ATTRIBUTE);
        if (hdAttribute != null && StringUtils.isNotEmpty(hdAttribute.getNodeValue())) {
            sportLobby.setTableId(hdAttribute.getNodeValue());
        }

        //TODO
        DomNode parentNode = htmlAnchor.getParentNode();
        if (parentNode != null
                && parentNode.getParentNode() != null
                && parentNode.getParentNode().getParentNode() != null
                && parentNode.getParentNode().getParentNode().getFirstChild() != null
                && StringUtils.isNotEmpty(parentNode.getParentNode().getParentNode().getFirstChild().asText())) {
            sportLobby.setSportName(parentNode.getParentNode().getParentNode().getFirstChild().asText());
        }

        return sportLobby;

    }

    public void likeAMain() {
        List<SportLobby> leftMenuObjects = getListOfSportLobby(XPATH_NAVIGATIONAL_MENU, SEARCH_URL);

        if (leftMenuObjects == null || leftMenuObjects.isEmpty()) {
            return;
        }

        List<SportLobby> allTableFiledsList = new ArrayList<>();

        long t = System.currentTimeMillis();
//        allTableFiledsList = leftMenuObjects
//                .parallelStream()
//                .unordered()
//                .map(leftMenuObject
//                        -> some("https://pm.by" + leftMenuObject.getHref(), leftMenuObject.getTableId()))
//                .flatMap(Collection::parallelStream)
//                .collect(Collectors.toList());
//        for (LeftMenuObject leftMenuObject : leftMenuObjects) {
//            allTableFiledsList.addAll(some("https://pm.by" + leftMenuObject.getHref(), leftMenuObject.getTableId()));
//        }


        allTableFiledsList = leftMenuObjects
                .parallelStream()
                .unordered()
                .map(this::some)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());


        System.out.println("Total: " + (System.currentTimeMillis() - t) / 1000);
        allTableFiledsList.size();

        client.close();
    }

    private List<HtmlTableBody> getBetInfoTableBodies(HtmlPage contentPage, String tableId) {
        List<DomElement> contentTables = contentPage.getElementsByTagName(TABLE_TAG_VALUE);

        DomElement tableDomeElement = contentTables.stream().filter(Objects::nonNull).filter(domElement -> StringUtils.isNotEmpty(domElement.getAttribute(ID_ATTRIBUTE))
                && domElement.getAttribute(ID_ATTRIBUTE).contains(tableId))
                .findFirst().orElse(null);

        HtmlTable htmlTableContent = tableDomeElement instanceof HtmlTable ? (HtmlTable) tableDomeElement : null;


        if (htmlTableContent != null && htmlTableContent.getBodies() != null && htmlTableContent.getBodies().size() == 1) {

            List<HtmlTableBody> collectHtmlTableBodies = new ArrayList<>();

            for (HtmlTableCell tableCell : htmlTableContent.getRows().get(0).getCells()) {
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
        return !htmlTable.getAttribute(STYLE_ATTRIBUTE).contains(NON_DISPLAY_ATTRIBUTE)
                //   &&! item.getAttributes().getNamedItem("style").getNodeValue()
                && (htmlTable.getAttributes() != null && htmlTable.getAttributes().getNamedItem(ID_ATTRIBUTE) == null)
                && !htmlTable.getAttribute(CLASS_ATTRIBUTE).contains(SPACE_ATTRIBUTE);
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

    private SportLobby some(SportLobby sportLobby) {
        long t = System.currentTimeMillis();

        HtmlPage contentPage = null;

        //TODO пересмотреть
        String contentUrl = SEARCH_URL + HD_CONTENT_URL + sportLobby.getTableId() + HS_PARAM_URL;

        try {
            contentPage = client.getPage(contentUrl);
        } catch (IOException e) {
            e.printStackTrace();
            logger.log(Level.WARNING, e.getMessage());
        }

        if (contentPage == null) {
            return null;
        }

        List<HtmlTableBody> betInfoTableBodies = getBetInfoTableBodies(contentPage, sportLobby.getTableId());

        //TODO может и лишнее
        List<HtmlTableBody> headTableBodies = betInfoTableBodies.stream()
                .filter(tableBody -> isContainsCellValue(tableBody, P1_TITLE) || isContainsCellValue(tableBody, P2_TITLE)).collect(Collectors.toList());

        if (headTableBodies.isEmpty()) {
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
                    continue;
                }

                BetInfo betInfo = fillBetInfo(indexMap, betInfoValues);

                infoList.add(betInfo);
            }
        } catch (Exception e) {
            e.getMessage();
        }
        System.out.println("Complete: " + (System.currentTimeMillis() - t) / 1000);

        sportLobby.setBetInfoList(infoList);

        return sportLobby;

    }

    private BetInfo fillBetInfo(Map<String, Integer> indexMap, List<String> cellsData) throws ParseException {
        BetInfo betInfo = new BetInfo();
        List<CoeffInfo> coeffInfos = new ArrayList<>();
        if (indexMap.get(DATE_TITLE) != null) {
            betInfo.setDate((new SimpleDateFormat(DATE_FORMAT_PATTERN).parse(LocalDate.now().getYear() + "-" + cellsData.get(indexMap.get(DATE_TITLE)).replace("/", "-").replace("\r", "").replace("\n", " ")).getTime()));
        }

        if (indexMap.get(EVENT_TITLE) != null) {
            betInfo.setHomeTeam(cellsData.get(indexMap.get(EVENT_TITLE)).split("\r\n")[0]);
            betInfo.setVisitTeam(cellsData.get(indexMap.get(EVENT_TITLE)).split("\r\n")[1]);
        }
        if (indexMap.get(P1_TITLE) != null) {
            CoeffInfo coeffInfo = new CoeffInfo(P1_TITLE);
            if (!cellsData.get(indexMap.get(P1_TITLE)).isEmpty()) {
                coeffInfo.setValue(Double.valueOf(cellsData.get(indexMap.get(P1_TITLE))));
            }
            coeffInfos.add(coeffInfo);
        }
        if (indexMap.get(X_TITLE) != null) {
            CoeffInfo coeffInfo = new CoeffInfo(X_TITLE);
            if (!cellsData.get(indexMap.get(X_TITLE)).isEmpty()) {
                coeffInfo.setValue(Double.valueOf(cellsData.get(indexMap.get(X_TITLE))));
            }
            coeffInfos.add(coeffInfo);
        }
        if (indexMap.get(P2_TITLE) != null) {
            CoeffInfo coeffInfo = new CoeffInfo(P2_TITLE);
            if (!cellsData.get(indexMap.get(P2_TITLE)).isEmpty()) {
                coeffInfo.setValue(Double.valueOf(cellsData.get(indexMap.get(P2_TITLE))));
            }
            coeffInfos.add(coeffInfo);
        }

        betInfo.setCoefficients(coeffInfos);

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
