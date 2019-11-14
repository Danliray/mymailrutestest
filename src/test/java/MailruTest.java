import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Configuration.baseUrl;
import static com.codeborne.selenide.Configuration.browser;
import static com.codeborne.selenide.Selectors.*;
import static com.codeborne.selenide.Selenide.*;


public class MailruTest {

    /*Если нужен firefox (хром по-умолчанию)
    @BeforeEach
    public void setBrowser() {
        browser = "firefox";
    }*/

    @Test
    public void enterUser() {
        int numMails = 6;
        String email = "testovyy.akkaunt2@bk.ru";
        String subj = "Тестовое задание Selenide";,
        String bodyMail = "Тело письма Selenide";,
        String myUrlFile = "C:\\selenide\\mdm.txt";
        Configuration.holdBrowserOpen = true;
        //Login
        open("https://mail.ru/");
        $(By.name("login")).setValue("testovyy.akkaunt1@bk.ru").pressEnter();
        $(By.name("password")).setValue("test.akka1").pressEnter();

        loginUser();
        //Проверка авторизации
        $$("i.x-ph__menu__button__text_auth").findBy(text("testovyy.akkaunt2@bk.ru"));

        //Configuration.timeout = 10000;
        sendMail(numMails, email, subj, bodyMail, myUrlFile);


    }

    public void loginUser() {

    }

    public void sendMail(int numMails, String email, String subj, String bodyMail, String myUrlFile) {
        for (int i = 1; i <= numMails; i++) {
            try {
                //Кликаю кнопку "Написать письмо"
                $$("#b-toolbar__left span")
                        .findBy(text("Написать письмо")).click();

                //Получатель
                $(byXpath("//textarea[@data-original-name=\"To\"]")).setValue(email);

                //Тема письма
                $(By.name("Subject")).setValue(subj);

                //Тело письма:
                SelenideElement mce = $("#tinymce");
                //Переключаю фрейм
                switchTo().frame($(".mceLayout").$(By.tagName("iframe")));
                mce.click();
                mce.clear();
                mce.sendKeys(bodyMail);
                //в дефолтный фрейм
                switchTo().defaultContent();

                //прикрепляю файл mdm с текстом mdm-система
                $(byXpath("//input[@type=\"file\"]")).sendKeys(myUrlFile);

                //Кликаю кнопку "Отправить"
                $$("#b-toolbar__right span")
                        .findBy(text("Отправить")).click();

                //проверка отправки письма
                $("div.message-sent__title").shouldBe(exactText("Ваше письмо отправлено. Перейти во Входящие"));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

/*----------------Пробы пера---------------------*/

//.shouldNotBe(visible);
//$(byXpath("//a[@rel=\"history\"]")).waitWhile((text(" отправлено. ")), 10000);
//Кликаю кнопку "Написать письмо"
//Configuration.timeout = 6000;
//$(".b-nav__link").$(byXpath("//a[@href=\"/messages/inbox/\"]")).click();
//$("i#PH_user-email").shouldBe(text("testovyy.akkaunt1@bk.ru"));
//$("i#PH_user-email").click();
//System.out.println($(".b-layout"));
//$$(".b-layout ").findBy(text("Написать письмо")).click();
//$(byText("Написать письмо")).click();
//$(byXpath("//a[@data-title=\"Написать письмо (N)\"]")).click();
//b-toolbar__btn_with-foldings js-shortcut
//$(byXpath("//a[@data-toolbar-group=\"left\"]")).$(byText("Написать письмо")).click();
//a[@rel=\"history\"]
//data-title="Написать письмо (N)"
//$$("i.x-ph__menu__button__text_auth").findBy(text("testovyy.akkaunt2@bk.ru"));
// $$("span.b-nav__item__text").find(text("Написать письмо"));
//$(".b-nav__link span").shouldHave(text("Входящие"));
//Переход в отправленные
//$(byXpath("//a[@href=\"/messages/sent/\"]")).click();
//$(byName("Filedata")).sendKeys(url);
//$("div.compose-attachments__tools")
//        .find(By.tagName("input"))
//        .shouldBe(name("File"))
//                (byXpath("//input[@type=\"file\"]")).sendKeys(url);
//$("div").shouldHave(cssClass("js-input-file"), cssClass("compose-attachments__item").("input").shouldHave(type("file")).sendKeys(url);
//$(byText("Прикрепить файл")).click();
//$("input").shouldHave(type("checkbox")).sendKeys(ur);
//$(urlbaseUrl).shouldBe(exactText("sendmsgok"));
//$(byXpath("//a[@data-name=\"compose\"]")).click();
//SelenideElement mceframe = $(".mceLayout").$(By.tagName("iframe"));
//SelenideElement PH = $("#PH_authMenu_button").$("#PH_user-email");
//$(PH).waitUntil(Condition.exactText(("testovyy.akkaunt1@bk.ru")), 20000);

//PH.shouldBe(text("testovyy.akkaunt1@bk.ru"));
//SelenideElement PH = $("#PH_user-email").shouldBe(enabled).
//       .$(byText("testovyy.akkaunt1@bk.ru"))
//       .shouldBe(visible);