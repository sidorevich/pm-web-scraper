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
import com.webfirmframework.wffweb.tag.htmlwff.NoTag;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.util.Const.*;

public class HtmlGenerator {
    private static final Logger logger = Logger.getLogger(HtmlGenerator.class.getName());

    /**
     * Сформировать html страницу с данными и записать
     *
     * @param sportLobbies Полученные из парсинга данные
     */
    public static void writeToFileReceivedData(List<SportLobby> sportLobbies) {
        String htmlContent = getHtmContent(sportLobbies);
        writeFile(htmlContent);
    }

    /**
     * Получить html страницу с заполненной данными таблицей
     *
     * @param sportLobbies {@link SportLobby} полуученные данные
     * @return html страница
     */
    private static String getHtmContent(List<SportLobby> sportLobbies) {
        try {
            Html htmlPage = new Html(null) {
                @Override
                protected void init() {
                    new Body(this) {
                        @Override
                        protected void init() {
                            new Table(this, new CustomAttribute(BORDER_ATTRIBUTE, "1px")) {
                                {
                                    new Th(this) {
                                        @Override
                                        protected void init() {
                                            new NoTag(this, KIND_SPORT_TITLE);
                                        }
                                    };
                                    new Th(this) {
                                        @Override
                                        protected void init() {
                                            new NoTag(this, EVENT_TABLE_TITLE);
                                        }
                                    };
                                    new Th(this) {
                                        @Override
                                        protected void init() {
                                            new NoTag(this, HOME_TEAM_TABLE_TITLE);
                                        }
                                    };
                                    new Th(this) {
                                        @Override
                                        protected void init() {
                                            new NoTag(this, VISIT_TEAM_TABLE_TITLE);
                                        }
                                    };
                                    new Th(this) {
                                        @Override
                                        protected void init() {
                                            new NoTag(this, DATE_START_TABLE_TITLE);
                                        }
                                    };
                                    new Th(this) {
                                        @Override
                                        protected void init() {
                                            new NoTag(this, P1_TITLE);
                                        }
                                    };
                                    new Th(this) {
                                        @Override
                                        protected void init() {
                                            new NoTag(this, X_TITLE);
                                        }
                                    };
                                    new Th(this) {
                                        @Override
                                        protected void init() {
                                            new NoTag(this, P2_TITLE);
                                        }
                                    };
                                    new Th(this) {
                                        @Override
                                        protected void init() {
                                            new NoTag(this, LINK_TABLE_TITLE);
                                        }
                                    };
                                    for (final SportLobby sportLobby : sportLobbies) {
                                        for (BetInfo betInfo : sportLobby.getBetInfoList()) {
                                            new Tr(this) {
                                                @Override
                                                protected void init() {
                                                    new Td(this) {
                                                        @Override
                                                        protected void init() {
                                                            String sportName = sportLobby.getSportName() != null ? sportLobby.getSportName() : MISSING_VALUE;
                                                            new NoTag(this, sportName);
                                                        }
                                                    };
                                                    new Td(this) {
                                                        @Override
                                                        protected void init() {
                                                            String eventName = sportLobby.getName() != null ? sportLobby.getName() : MISSING_VALUE;
                                                            new NoTag(this, eventName);
                                                        }
                                                    };
                                                    new Td(this) {
                                                        @Override
                                                        protected void init() {
                                                            String homeTeam = betInfo.getHomeTeam() != null ? betInfo.getHomeTeam() : MISSING_VALUE;
                                                            new NoTag(this, homeTeam);
                                                        }
                                                    };
                                                    new Td(this) {
                                                        @Override
                                                        protected void init() {
                                                            String visitTeam = betInfo.getVisitTeam() != null ? betInfo.getVisitTeam() : MISSING_VALUE;
                                                            new NoTag(this, visitTeam);
                                                        }
                                                    };
                                                    new Td(this) {
                                                        @Override
                                                        protected void init() {
                                                            String dateLong = betInfo.getDate() != null ? betInfo.getDate().toString() : MISSING_VALUE;
                                                            new NoTag(this, dateLong);
                                                        }
                                                    };
                                                    new Td(this) {
                                                        @Override
                                                        protected void init() {
                                                            CoeffInfo winnerCoeffVal = betInfo.getCoefficients().get(P1_TITLE);
                                                            String winnerCoeff = (winnerCoeffVal != null && winnerCoeffVal.getValue() != null) ? winnerCoeffVal.getValue().toString() : MISSING_VALUE;
                                                            new NoTag(this, winnerCoeff);
                                                        }
                                                    };
                                                    new Td(this) {
                                                        @Override
                                                        protected void init() {
                                                            CoeffInfo drawCoeffVal = betInfo.getCoefficients().get(X_TITLE);
                                                            String drawCoeff = (drawCoeffVal != null && drawCoeffVal.getValue() != null) ? drawCoeffVal.getValue().toString() : MISSING_VALUE;
                                                            new NoTag(this, drawCoeff);
                                                        }
                                                    };
                                                    new Td(this) {
                                                        @Override
                                                        protected void init() {
                                                            CoeffInfo visitWinVal = betInfo.getCoefficients().get(P2_TITLE);
                                                            String visitWin = (visitWinVal != null && visitWinVal.getValue() != null) ? visitWinVal.getValue().toString() : MISSING_VALUE;
                                                            new NoTag(this, visitWin);
                                                        }
                                                    };
                                                    new Td(this) {
                                                        @Override
                                                        protected void init() {
                                                            String link = sportLobby.getLink() != null ? sportLobby.getLink() : MISSING_VALUE;
                                                            new A(this, new CustomAttribute(HREF_ATTRIBUTE, link)) {
                                                                @Override
                                                                protected void init() {
                                                                    new NoTag(this, link);
                                                                }
                                                            };
                                                        }
                                                    };
                                                }
                                            };
                                        }
                                    }
                                }
                            };
                        }
                    };
                }
            };

            htmlPage.setPrependDocType(true);

            return htmlPage.toHtmlString();
        } catch (Exception e) {
            logger.log(Level.SEVERE, ERROR_MSG, e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Записать ранее сформированную страницу
     *
     * @param htmlPageData сформированная страница
     */
    private static void writeFile(String htmlPageData) {
        if (htmlPageData == null) {
            logger.warning("No data to write!");
            return;
        }
        try {
            FileUtils.write(new File(REPORT_FILE_PATH), htmlPageData, "UTF-8");
        } catch (Exception e) {
            logger.log(Level.SEVERE, ERROR_MSG, e.getMessage());
            e.printStackTrace();
        }
    }
}
