import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.CollectionCondition.*;
import static com.codeborne.selenide.Configuration.baseUrl;
import static com.codeborne.selenide.Configuration.browser;
import static com.codeborne.selenide.Selectors.*;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;


public class MailruTest {

    /*Если нужен firefox (хром по-умолчанию)
    @BeforeEach
    public void setBrowser() {
        browser = "firefox";
    }*/

    @Test
    public void enterUser() {

        String loginUser1 = "testovyy.akkaunt1@bk.ru";//логин 1 аккаунта
        String passUser1 = "test.akka1";//пароль 1 аккаунта
        String loginUser2 = "testovyy.akkaunt2@bk.ru";//логин 2 аккаунта
        String passUser2 = "test.akka2";//пароль 2 аккаунта
        int numMails = 1; //количество писем
        String subj = "Тестовое задание Selenide"; //тема письма
        String bodyMail = "Тело письма Selenide"; //Тело письма
        String bodyMail2 = "Выполнено"; //Тело ответного письма
        String myUrlFile = "C:\\selenide\\mdm.txt"; //ссылка на прикрепляемый файл
        Configuration.holdBrowserOpen = true;

        //открываю 1 вкладку
        open("https://mail.ru/");

        //авторизация под первым аккаунтом и проверка авторизации
        autorizationUser1(loginUser1, passUser1);

        //отправляю письма на второй аккаунт и проверяю отправку писем
        sendMail(numMails, loginUser2, subj, bodyMail, myUrlFile);

        //переключаюсь на 2 вкладку
        JavascriptExecutor jse = (JavascriptExecutor)getWebDriver();
        jse.executeScript("window.open('https://mail.ru/');");
        switchTo().window(1);

        //нажатие на кнопку выбора аккаунта
        $("span#PH_authMenu_button").shouldBe(enabled).click();
        sleep(1000);
        $("a#PH_loginAnotherLink").shouldBe(enabled).click();

        //авторизация под вторым аккаунтом
        sleep(1000);
        switchTo().frame($(".ag-popup__frame__layout__iframe"));
        autorizationUser2(loginUser2, passUser2);

        //открываю группу писем
        //$("div.b-datalist__item__subj").shouldBe(text(subj)).click();

        //выбираю "только непрочитанные"
        $(byXpath("//div[@data-group=\"selectAll\"]")).$("div.b-dropdown__arrow").click();
        $$(".b-toolbar__group").findBy(text("Непрочитанные")).click();

        //открываю 3-е непрочитаннае письмо
        $("#b-letters .b-datalist__body > div:nth-child(3)").click();

        //Проверка темы, тела и аттача
        $("#b-thread .b-letter__head__subj__text").shouldHave(text(subj));
        $("#b-thread .js-readmsg-msg").shouldHave(text(bodyMail));
        $("#b-thread .attachment").shouldBe(enabled);
        //$("#b-thread span.attachment__content-text").shouldHave(text("mdm система"));

        //отвечаю на письмо на аккаунт 1
        $$("#b-toolbar__right span")
                .findBy(text("Ответить")).click();
        //Тело письма:
        mceEditorEdit(bodyMail2);


    }

    public void autorizationUser1(String loginUser, String passUser) {
        //авторизация под первым аккаунтом
        $(By.name("login")).shouldBe(exist).setValue(loginUser).pressEnter();
        $(By.name("password")).shouldBe(exist).setValue(passUser).pressEnter();
        //Проверка авторизации
        $$("i.x-ph__menu__button__text_auth").findBy(text(loginUser));
    }

    public void autorizationUser2(String loginUser, String passUser) {
        //авторизация под вторым аккаунтом
        $(By.name("Login")).should(exist).setValue(loginUser).pressEnter();
        $(By.name("Password")).should(exist).setValue(passUser).pressEnter();
        //Проверка авторизации
        $$("i.x-ph__menu__button__text_auth").findBy(text(loginUser));

    }

    public void mceEditorEdit(String bodyMail) {
        //ввожу тело письма:
        SelenideElement mce = $("#tinymce");
        //Переключаю фрейм
        switchTo().frame($(".mceLayout").$(By.tagName("iframe")));
        mce.click();
        mce.clear();
        mce.sendKeys(bodyMail);
        //в дефолтный фрейм
        switchTo().defaultContent();
    }


    public void sendMail(int numMails, String email, String subj, String bodyMail, String myUrlFile) {
        for (int i = 1; i <= numMails; i++) {

                //Кликаю кнопку "Написать письмо"
                $$("#b-toolbar__left span")
                        .findBy(text("Написать письмо")).click();

                //Получатель
                $(byXpath("//textarea[@data-original-name=\"To\"]")).setValue(email);

                //Тема письма
                $(By.name("Subject")).setValue(subj);

                //Тело письма:
                mceEditorEdit(bodyMail);

                //прикрепляю файл mdm с текстом mdm-система
                $(byXpath("//input[@type=\"file\"]")).sendKeys(myUrlFile);

                //Кликаю кнопку "Отправить"
                $$("#b-toolbar__right span")
                        .findBy(text("Отправить")).click();

                //проверка отправки письма
                $("div.message-sent__title").shouldBe(exactText("Ваше письмо отправлено. Перейти во Входящие"));
        }
    }
}

/*----------------Пробы пера---------------------*/

//        try {
//            } catch (Exception e) {
//                    e.printStackTrace();
//                    }
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
//Configuration.timeout = 10000;
//autorizationUser(loginUser1, passUser1);
//switchTo().defaultContent();
//switchTo().frame(3);
//$("input").click();
//sendKeys(loginUser2);
//switchTo().activeElement().sendKeys(loginUser2);

//switchTo().frame($(".mceLayout").$(By.tagName("iframe")));
//System.out.println(switchTo().frame($(".ag-popup__frame__layout__iframe")).getPageSource());
//switchTo().frame($(".ag-popup__frame__layout__iframe")).getPageSource();
//switchTo().frame($(".ag-popup__frame__layout__iframe"));
//switchTo().innerFrame($(ByTagName("frame")));
//System.out.println($$(By.tagName(frame);
//switchTo().activeElement().sendKeys(loginUser2);

//autorizationUser(loginUser2, passUser2);
//$("input").sendKeys(loginUser2);

//sendMail(numMails, email, subj, bodyMail, myUrlFile);
//$$(".username-formfield").shouldBe().findBy(type("text")).sendKeys(loginUser2);
//$("#root").find(By.xpath("//input[@autocomplete=\"username\"]")).setValue(loginUser2).pressEnter();
//$("#root").$(By.name("password")).setValue(passUser2).pressEnter();
//Ввод логина и пароля
//$(By.name("Login")).shouldBe(enabled).setValue(loginUser).pressEnter();
//switchTo().activeElement().sendKeys(loginUser);
//$(By.xpath("//input[@type=\"submit\"]")).click();
//switchTo().activeElement().sendKeys(Keys.ENTER);

//switchTo().activeElement().sendKeys(passUser);
//switchTo().activeElement().sendKeys(Keys.ENTER);


//Проверка авторизации
//$$("i.x-ph__menu__button__text_auth").findBy(text("testovyy.akkaunt2@bk.ru"));

//.$(".b-datalist__item_unread")
//.filterBy(withText("Selenide")
//.sh(text("Тестовое задание Selenide")).click();
//$)//.find(byCssSelector()".b-datalist__item")//.shouldHave(size(6))//.shouldHave(cssValue("font-weight", "bold;"))
//$("#b-letters .b-datalist__body > div:nth-child(3)")//.filterBy(cssClass("b-datalist__item_unread")
//        .$(".b-datalist__item_unread")
//       .find("");
//       .shouldHave().find().click();
//> div:nth-child(3)