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
            Html html = new Html(null) {
                Body body = new Body(this) {
                    Table table = new Table(this, new CustomAttribute("border",
                            "1px")) {
                        {
                            Th th1 = new Th(this) {
                                Blank cellContent = new Blank(this,
                                        "Вид спорта");
                            };
                            Th th2 = new Th(this) {
                                Blank cellContent = new Blank(this,
                                        "Турнир");
                            };
                            Th th3 = new Th(this) {
                                Blank cellContent = new Blank(this,
                                        "Домашная команда");
                            };
                            Th th4 = new Th(this) {
                                Blank cellContent = new Blank(this,
                                        "Команда гостей ");
                            };
                            Th th5 = new Th(this) {
                                Blank cellContent = new Blank(this,
                                        "Начало события");
                            };
                            Th th6 = new Th(this) {
                                Blank cellContent = new Blank(this,
                                        "П1");
                            };
                            Th th7 = new Th(this) {
                                Blank cellContent = new Blank(this,
                                        "Х");
                            };
                            Th th8 = new Th(this) {
                                Blank cellContent = new Blank(this,
                                        "П2");
                            };
                            Th th9 = new Th(this) {
                                Blank cellContent = new Blank(this,
                                        "Ссылка");
                            };
                            for (final SportLobby sportLobby : sportLobbies) {
                                for (BetInfo betInfo : sportLobby.getBetInfoList()) {
                                    Tr tr = new Tr(this) {
                                        Td td1 = new Td(this) {
                                            String sportName = sportLobby.getSportName() != null ? sportLobby.getSportName() : "-";
                                            Blank cellContent = new Blank(this, sportName);
                                        };
                                        Td td2 = new Td(this) {
                                            String eventName = sportLobby.getName() != null ? sportLobby.getName() : "-";
                                            Blank cellContent = new Blank(this, eventName);
                                        };
                                        Td td3 = new Td(this) {
                                            String homeTeam = betInfo.getHomeTeam() != null ? betInfo.getHomeTeam() : "-";
                                            Blank cellContent = new Blank(this, homeTeam);
                                        };
                                        Td td4 = new Td(this) {
                                            String visitTeam = betInfo.getVisitTeam() != null ? betInfo.getVisitTeam() : "-";
                                            Blank cellContent = new Blank(this, visitTeam);
                                        };
                                        Td td5 = new Td(this) {
                                            String dateLong = betInfo.getDate() != null ? betInfo.getDate().toString() : "-";
                                            Blank cellContent = new Blank(this, dateLong);

                                        };
                                        Td td6 = new Td(this) {
                                            CoeffInfo winnerCoeffVal = betInfo.getCoefficients().get(P1_TITLE);
                                            String winnerCoeff = (winnerCoeffVal != null && winnerCoeffVal.getValue() != null) ? winnerCoeffVal.getValue().toString() : "-";
                                            Blank cellContent = new Blank(this, winnerCoeff);


                                        };
                                        Td td7 = new Td(this) {
                                            CoeffInfo drawCoeffVal = betInfo.getCoefficients().get(X_TITLE);
                                            String drawCoeff = (drawCoeffVal != null && drawCoeffVal.getValue() != null) ? drawCoeffVal.getValue().toString() : "-";
                                            Blank cellContent = new Blank(this, drawCoeff);

                                        };
                                        Td td8 = new Td(this) {
                                            CoeffInfo visitWinVal = betInfo.getCoefficients().get(P2_TITLE);
                                            String visitWin = (visitWinVal != null && visitWinVal.getValue() != null) ? visitWinVal.getValue().toString() : "-";
                                            Blank cellContent = new Blank(this, visitWin);

                                        };
                                        Td td9 = new Td(this) {
                                            String link = sportLobby.getLink() != null ? sportLobby.getLink() : "-";
                                            A a = new A(this, new CustomAttribute("href", link)) {
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

            html.setPrependDocType(true);

            return html.toHtmlString();
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
