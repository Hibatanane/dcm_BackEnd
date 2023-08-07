package prjt.dcm.Helpers;

import java.io.IOException;


public class HTML {
    public static String htmlEmailTemplate() throws IOException {

        String url = "http://localhost:4200/reinitialiserMdp";
        String imageUrl = "http://localhost:8080/assets/Logo.png";
        String emailTemplate = "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "  <head>\n" +
                "    <meta charset=\"UTF-8\" />\n" +
                "    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\" />\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\" />\n" +
                "    <title>Email</title>\n" +
                "    <style>\n" +
                "      * {\n" +
                "        box-sizing: border-box;\n" +
                "        font-family: \"Courier New\", Courier, monospace;\n" +
                "      }\n" +
                "      .image {\n" +
                "        text-align: center;\n" +
                "        justify-content: center;\n" +
                "        align-items: center;\n" +
                "        display: flex;\n" +
                "      }\n" +
                "\n" +
                "      body {\n" +
                "        height: 100vh;\n" +
                "        background-color: rgba(241, 241, 241, 0.911);\n" +
                "        display: flex;\n" +
                "        align-items: center;\n" +
                "        justify-content: center;\n" +
                "      }\n" +
                "\n" +
                "      .wrapper {\n" +
                "        width: 550px;\n" +
                "        height: auto;\n" +
                "        padding: 15px;\n" +
                "        background-color: white;\n" +
                "        border-radius: 7px;\n" +
                "        box-shadow: 1px 5px 2px 3px rgba(0, 0, 0, 0.5);\n" +
                "      }\n" +
                "\n" +
                "      .email-msg-header {\n" +
                "        text-align: center;\n" +
                "      }\n" +
                "\n" +
                "      .app-name {\n" +
                "        width: 100%;\n" +
                "        font-size: 25px;\n" +
                "        color: black;\n" +
                "        text-align: center;\n" +
                "      }\n" +
                "\n" +
                "      .app-text {\n" +
                "        text-align: center;\n" +
                "        display: flex;\n" +
                "        align-items: center;\n" +
                "        justify-content: center;\n" +
                "        text-decoration: underline;\n" +
                "\n" +
                "      }\n" +
                "\n" +
                "      .verify-account-btn {\n" +
                "        padding: 10px;\n" +
                "        background-color: #ccccff;\n" +
                "        text-align: center;\n" +
                "        color: #00004c;\n" +
                "        border-radius: 5px;\n" +
                "        align-items: center;\n" +
                "        display: flex;\n" +
                "        justify-content: center;\n" +
                "        width: 100px;\n" +
                "        text-decoration: none;\n" +
                "        margin-top: 10px;\n" +
                "        left: 50%;\n" +
                "        transform: translate(-50%, -50%);\n" +
                "        position: absolute;\n" +
                "      }\n" +
                "    </style>\n" +
                "  </head>\n" +
                "\n" +
                "  <body>\n" +
                "    <div class=\"wrapper\">\n" +
                "      <div class=\"logo ms-4\">\n" +
                "        <img src=\"cid:logoImage\" alt=\"Logo\">\n" +
                "      </div>\n" +
                "      <hr />\n" +
                "      <div class=\"container\">\n" +
                "        <p class=\"app-text\">Reinitialiser mdp</p>\n" +
                "        <a href=" + url + " class=\"verify-account-btn\" role=\"button\">Valider</a>\n" +
                "        <br>\n" +
                "        <br>\n" +
                "        <p>\n" +
                "          Si vous n'avez pas oublié votre mot de passe, vous pouvez ignorer cet\n" +
                "          e-mail.\n" +
                "        </p>\n" +
                "      </div>\n" +
                "    </div>\n" +
                "  </body>\n" +
                "</html>\n";


        return emailTemplate;
    }
}
