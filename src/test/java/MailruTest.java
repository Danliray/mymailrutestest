import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Configuration.browser;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class MailruTest {

    @BeforeEach
    public void setBrowser() {
        browser = "chrome";
    }

    @Test
    public void enterUser() {
        open("https://mail.ru/");
        $(By.name("login")).setValue("testovyy.akkaunt1@bk.ru").pressEnter();
        $(By.name("password")).setValue("test.akka1").pressEnter();
        //$("#PH_user-email").shouldHave(text("testovyy.akkaunt1@bk.ru"));
        //$(By.id("PH_user-email")).shouldHave(text("testovyy.akkaunt1@bk.ru"));

        $(byText("Написать письмо")).click();
        $(By.name("To")).setValue("Тестовое задание Selenide");


    }
}
