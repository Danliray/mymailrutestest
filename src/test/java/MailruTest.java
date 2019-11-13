import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Configuration.browser;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selectors.byXpath;
import static com.codeborne.selenide.Selenide.*;


public class MailruTest {

    /*Если нужен firefox
    @BeforeEach
    public void setBrowser() {
        browser = "firefox";
    }*/

    @Test
    public void enterUser() {
        //Login
        open("https://mail.ru/");
        $(By.name("login")).setValue("testovyy.akkaunt1@bk.ru").pressEnter();
        $(By.name("password")).setValue("test.akka1").pressEnter();

        //Кликаю кнопку "Написать письмо"
        $(byText("Написать письмо")).click();

        //Получатель
        $(byXpath("//textarea[@data-original-name=\"To\"]")).setValue("testovyy.akkaunt2@bk.ru");

        //Тема письма
        $(By.name("Subject")).setValue("Тестовое задание Selenide");

        //Тело письма:
        SelenideElement mceframe = $(".mceLayout").$(By.tagName("iframe"));
        SelenideElement mce = $("#tinymce");
        //Переключаю фрейм
        switchTo().frame(mceframe);
        mce.click();
        mce.clear();
        mce.sendKeys("Тело письма Selenide");
        //в дефолтный фрейм
        switchTo().defaultContent();



        //SelenideElement PH = $(By.id("PH_user-email"));
        //SelenideElement PH = $("i#PH_user-email");
        //$(PH).waitUntil(Condition.exactText(("testovyy.akkaunt1@bk.ru")), 20000);
        //PH.shouldBe(text("testovyy.akkaunt1@bk.ru"));
        //$(byXpath("//a[@data-name=\"compose\"]")).click();
    }
}
