/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lenny.my_shop_web_backend.ejb;

import com.lenny.my_shop_web_backend.ejb_db.User_DbBean;
import com.lenny.my_shop_web_backend.entities.User;
import com.lenny.my_shop_web_backend.jpa.TransactionProvider;
import com.lenny.my_shop_web_backend.utilities.Mail;
import javax.ejb.Asynchronous;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author lenny
 */
@Stateless
public class MailBean {

    @EJB
    private User_DbBean userDbBean;

    @EJB
    private TransactionProvider provider;

    @Asynchronous
    public void sendActivationEmail(String urlPrefix, String email, String name) {
        if (email != null) {
            if (name == null) {
                User user = userDbBean.getUser_ByEmail(email);
                if (user != null) {
                    name = user.getName();
                }
            } else {
                String link = urlPrefix + "api/user/activate/";
                String subject = "Activate your Account";
                String message = " <!DOCTYPE html>\n"
                        + "<html>\n"
                        + "\n"
                        + "<head>\n"
                        + "    <meta charset=\"utf-8\">\n"
                        + "    <meta http-equiv=\"x-ua-compatible\" content=\"ie=edge\">\n"
                        + "    <title>Email Confirmation</title>\n"
                        + "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n"
                        + "    <style type=\"text/css\">\n"
                        + "        @media screen {\n"
                        + "            @font-face {\n"
                        + "                font-family: 'Source Sans Pro';\n"
                        + "                font-style: normal;\n"
                        + "                font-weight: 400;\n"
                        + "                src: local('Source Sans Pro Regular'), local('SourceSansPro-Regular'), url(https://fonts.gstatic.com/s/sourcesanspro/v10/ODelI1aHBYDBqgeIAH2zlBM0YzuT7MdOe03otPbuUS0.woff) format('woff');\n"
                        + "            }\n"
                        + "\n"
                        + "            @font-face {\n"
                        + "                font-family: 'Source Sans Pro';\n"
                        + "                font-style: normal;\n"
                        + "                font-weight: 700;\n"
                        + "                src: local('Source Sans Pro Bold'), local('SourceSansPro-Bold'), url(https://fonts.gstatic.com/s/sourcesanspro/v10/toadOcfmlt9b38dHJxOBGFkQc6VGVFSmCnC_l7QZG60.woff) format('woff');\n"
                        + "            }\n"
                        + "        }\n"
                        + "\n"
                        + "        body,\n"
                        + "        table,\n"
                        + "        td,\n"
                        + "        a {\n"
                        + "            -ms-text-size-adjust: 100%;\n"
                        + "            -webkit-text-size-adjust: 100%;\n"
                        + "        }\n"
                        + "\n"
                        + "        table,\n"
                        + "        td {\n"
                        + "            mso-table-rspace: 0pt;\n"
                        + "            mso-table-lspace: 0pt;\n"
                        + "        }\n"
                        + "\n"
                        + "        img {\n"
                        + "            -ms-interpolation-mode: bicubic;\n"
                        + "        }\n"
                        + "\n"
                        + "        a[x-apple-data-detectors] {\n"
                        + "            font-family: inherit !important;\n"
                        + "            font-size: inherit !important;\n"
                        + "            font-weight: inherit !important;\n"
                        + "            line-height: inherit !important;\n"
                        + "            color: inherit !important;\n"
                        + "            text-decoration: none !important;\n"
                        + "        }\n"
                        + "\n"
                        + "        div[style*=\"margin: 16px 0;\"] {\n"
                        + "            margin: 0 !important;\n"
                        + "        }\n"
                        + "\n"
                        + "        body {\n"
                        + "            width: 100% !important;\n"
                        + "            height: 100% !important;\n"
                        + "            padding: 0 !important;\n"
                        + "            margin: 0 !important;\n"
                        + "        }\n"
                        + "\n"
                        + "        table {\n"
                        + "            border-collapse: collapse !important;\n"
                        + "        }\n"
                        + "\n"
                        + "        a {\n"
                        + "            color: #1a82e2;\n"
                        + "        }\n"
                        + "\n"
                        + "        img {\n"
                        + "            height: auto;\n"
                        + "            line-height: 100%;\n"
                        + "            text-decoration: none;\n"
                        + "            border: 0;\n"
                        + "            outline: none;\n"
                        + "        }\n"
                        + "    </style>\n"
                        + "</head>\n"
                        + "\n"
                        + "<body style=\"background-color: #e9ecef;\">\n"
                        + "    <div class=\"preheader\"\n"
                        + "        style=\"display: none; max-width: 0; max-height: 0; overflow: hidden; font-size: 1px; line-height: 1px; color: #fff; opacity: 0;\">\n"
                        + "    </div>\n"
                        + "    <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\n"
                        + "        <tr>\n"
                        + "            <td align=\"center\" bgcolor=\"#e9ecef\">\n"
                        + "                <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"max-width: 600px;\">\n"
                        + "                    <tr>\n"
                        + "                        <td align=\"left\" valign=\"top\" style=\"padding: 36px 24px;\">\n"
                        + "\n"
                        + "                        </td>\n"
                        + "                    </tr>\n"
                        + "                </table>\n"
                        + "            </td>\n"
                        + "        </tr>\n"
                        + "        <tr>\n"
                        + "            <td align=\"center\" bgcolor=\"#e9ecef\">\n"
                        + "                <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"max-width: 600px;\">\n"
                        + "                    <tr>\n"
                        + "                        <td align=\"left\" bgcolor=\"#ffffff\"\n"
                        + "                            style=\"padding: 36px 24px 0; font-family: 'Source Sans Pro', Helvetica, Arial, sans-serif; border-top: 3px solid #d4dadf;\">\n"
                        + "                            <h2\n"
                        + "                                style=\"margin: 0; font-size: 32px; font-weight: 700; letter-spacing: -1px; line-height: 48px;\">\n"
                        + "                                Activate your My-Shop account</h2>\n"
                        + "                        </td>\n"
                        + "                    </tr>\n"
                        + "                </table>\n"
                        + "            </td>\n"
                        + "        </tr>\n"
                        + "        <tr>\n"
                        + "            <td align=\"center\" bgcolor=\"#e9ecef\">\n"
                        + "                <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"max-width: 600px;\">\n"
                        + "                    <tr>\n"
                        + "                        <td align=\"left\" bgcolor=\"#ffffff\"\n"
                        + "                            style=\"padding: 24px; font-family: 'Source Sans Pro', Helvetica, Arial, sans-serif; font-size: 16px; line-height: 24px;\">\n"
                        + "                            <p style=\"margin: 0;\">Hi " + name + ",</p>\n"
                        + "\n<br>"
                        + "                            <p style=\"margin: 0;\">\n"
                        + "\n"
                        + "                                To activate your account and verify your email address, please click 'Activate Account'\n"
                        + "                                button\n"
                        + "                                below:\n"
                        + "                            </p>\n"
                        + "                        </td>\n"
                        + "                    </tr>\n"
                        + "                    <tr>\n"
                        + "                        <td align=\"left\" bgcolor=\"#ffffff\">\n"
                        + "                            <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\n"
                        + "                                <tr>\n"
                        + "                                    <td align=\"center\" bgcolor=\"#ffffff\" style=\"padding: 12px;\">\n"
                        + "                                        <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\">\n"
                        + "                                            <tr>\n"
                        + "                                                <td align=\"center\" bgcolor=\"#1a82e2\" style=\"border-radius: 6px;\">\n"
                        + "                                                    <a href=\"" + link + "\" target=\"_blank\"\n"
                        + "                                                        style=\"display: inline-block; padding: 16px 36px; font-family: 'Source Sans Pro', Helvetica, Arial, sans-serif; font-size: 16px; color: #ffffff; text-decoration: none; border-radius: 6px;\">\n"
                        + "                                                        Activate Account</a>\n"
                        + "                                                </td>\n"
                        + "                                            </tr>\n"
                        + "                                        </table>\n"
                        + "                                    </td>\n"
                        + "                                </tr>\n"
                        + "                            </table>\n"
                        + "                        </td>\n"
                        + "                    </tr>\n"
                        + "                    <tr>\n"
                        + "                        <td align=\"left\" bgcolor=\"#ffffff\"\n"
                        + "                            style=\"padding: 24px; font-family: 'Source Sans Pro', Helvetica, Arial, sans-serif; font-size: 16px; line-height: 24px;\">\n"
                        + "                        </td>\n"
                        + "                    </tr>\n"
                        + "                </table>\n"
                        + "            </td>\n"
                        + "        </tr>\n"
                        + "        <tr>\n"
                        + "            <td align=\"center\" bgcolor=\"#e9ecef\">\n"
                        + "                <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"max-width: 600px;\">\n"
                        + "                    <tr>\n"
                        + "                        <td align=\"left\" valign=\"top\" style=\"padding: 36px 24px;\">\n"
                        + "\n"
                        + "                        </td>\n"
                        + "                    </tr>\n"
                        + "                </table>\n"
                        + "            </td>\n"
                        + "        </tr>\n"
                        + "    </table>\n"
                        + "</body>\n"
                        + "\n"
                        + "</html>";

                Mail mail = new Mail();
                mail.sendGmail(email, subject, message, null);
            }
        }
    }

}
