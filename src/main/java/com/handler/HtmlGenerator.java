package com.handler;

import com.data.model.BetInfo;
import com.data.model.CoeffInfo;
import com.data.model.SportLobby;
import com.webfirmframework.wffweb.tag.html.Body;
import com.webfirmframework.wffweb.tag.html.Html;
import com.webfirmframework.wffweb.tag.html.attributewff.CustomAttribute;
import com.webfirmframework.wffweb.tag.html.links.A;
import com.webfirmframework.wffweb.tag.html.tables.Table;
import com.webfirmframework.wffweb.tag.html.tables.Td;
import com.webfirmframework.wffweb.tag.html.tables.Th;
import com.webfirmframework.wffweb.tag.html.tables.Tr;
import com.webfirmframework.wffweb.tag.htmlwff.Blank;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.data.model.Const.*;

public class HtmlGenerator {
    private static final Logger logger = Logger.getLogger(HtmlGenerator.class.getName());

    public static void writeToFileReceivedData(List<SportLobby> sportLobbies) {
        String htmlContent = getHtmContent(sportLobbies);
        writeFile(htmlContent);
    }

    private static String getHtmContent(List<SportLobby> sportLobbies) {
        try {
            Html htmlPage = new Html(null) {
                Body body = new Body(this) {
                    Table table = new Table(this, new CustomAttribute(BORDER_ATTRIBUTE, "1px")) {
                        {
                            Th kindSportTh = new Th(this) {
                                Blank cellContent = new Blank(this, KIND_SPORT_TITLE);
                            };
                            Th eventTh = new Th(this) {
                                Blank cellContent = new Blank(this, EVENT_TABLE_TITLE);
                            };
                            Th homeTeamTh = new Th(this) {
                                Blank cellContent = new Blank(this, HOME_TEAM_TABLE_TITLE);
                            };
                            Th visitTeamTh = new Th(this) {
                                Blank cellContent = new Blank(this, VISIT_TEAM_TABLE_TITLE);
                            };
                            Th eventDateTh = new Th(this) {
                                Blank cellContent = new Blank(this, DATE_START_TABLE_TITLE);
                            };
                            Th homeTeamWinCoeffTh = new Th(this) {
                                Blank cellContent = new Blank(this, P1_TITLE);
                            };
                            Th drawCoeffTh = new Th(this) {
                                Blank cellContent = new Blank(this, X_TITLE);
                            };
                            Th visitTeamCoeffTh = new Th(this) {
                                Blank cellContent = new Blank(this, P2_TITLE);
                            };
                            Th linkTh = new Th(this) {
                                Blank cellContent = new Blank(this, LINK_TABLE_TITLE);
                            };
                            for (final SportLobby sportLobby : sportLobbies) {
                                for (BetInfo betInfo : sportLobby.getBetInfoList()) {
                                    Tr tr = new Tr(this) {
                                        Td kindSportTd = new Td(this) {
                                            String sportName = sportLobby.getSportName() != null ? sportLobby.getSportName() : MISSING_VALUE;
                                            Blank cellContent = new Blank(this, sportName);
                                        };
                                        Td eventTd = new Td(this) {
                                            String eventName = sportLobby.getName() != null ? sportLobby.getName() : MISSING_VALUE;
                                            Blank cellContent = new Blank(this, eventName);
                                        };
                                        Td homeTeamTd = new Td(this) {
                                            String homeTeam = betInfo.getHomeTeam() != null ? betInfo.getHomeTeam() : MISSING_VALUE;
                                            Blank cellContent = new Blank(this, homeTeam);
                                        };
                                        Td visitTeamTd = new Td(this) {
                                            String visitTeam = betInfo.getVisitTeam() != null ? betInfo.getVisitTeam() : MISSING_VALUE;
                                            Blank cellContent = new Blank(this, visitTeam);
                                        };
                                        Td eventDateTd = new Td(this) {
                                            String dateLong = betInfo.getDate() != null ? betInfo.getDate().toString() : MISSING_VALUE;
                                            Blank cellContent = new Blank(this, dateLong);

                                        };
                                        Td homeTeamWinCoeffTd = new Td(this) {
                                            CoeffInfo winnerCoeffVal = betInfo.getCoefficients().get(P1_TITLE);
                                            String winnerCoeff = (winnerCoeffVal != null && winnerCoeffVal.getValue() != null) ? winnerCoeffVal.getValue().toString() : MISSING_VALUE;
                                            Blank cellContent = new Blank(this, winnerCoeff);


                                        };
                                        Td drawCoeffTd = new Td(this) {
                                            CoeffInfo drawCoeffVal = betInfo.getCoefficients().get(X_TITLE);
                                            String drawCoeff = (drawCoeffVal != null && drawCoeffVal.getValue() != null) ? drawCoeffVal.getValue().toString() : MISSING_VALUE;
                                            Blank cellContent = new Blank(this, drawCoeff);

                                        };
                                        Td visitTeamCoeffTd = new Td(this) {
                                            CoeffInfo visitWinVal = betInfo.getCoefficients().get(P2_TITLE);
                                            String visitWin = (visitWinVal != null && visitWinVal.getValue() != null) ? visitWinVal.getValue().toString() : MISSING_VALUE;
                                            Blank cellContent = new Blank(this, visitWin);

                                        };
                                        Td linkTd = new Td(this) {
                                            String link = sportLobby.getLink() != null ? sportLobby.getLink() : MISSING_VALUE;
                                            A a = new A(this, new CustomAttribute(HREF_ATTRIBUTE, link)) {
                                                Blank cellContent = new Blank(this, link);
                                            };
                                        };
                                    };
                                }
                            }
                        }
                    };
                };
            };

            htmlPage.setPrependDocType(true);

            return htmlPage.toHtmlString();
        } catch (Exception e) {
            logger.log(Level.SEVERE, ERROR_MSG, e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    private static void writeFile(String data) {
        if (data == null) {
            logger.warning("No data to write!");
            return;
        }
        try {
            FileUtils.write(new File(REPORT_FILE_PATH), data, "UTF-8");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error message: ", e.getMessage());
            e.printStackTrace();
        }
    }
}
