package org.example;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;

public class App {
    WebDriver driver = new ChromeDriver();;
    String url = "https://rozetka.com.ua";

    @BeforeEach
    public void setUp() {
        driver.manage().window().maximize();
        driver.get(url);
    }

    @Test
    public void testSearchField() {
        //  	Введення даних у поле та перевірка поля на наявність даних;
        WebElement searchField = driver.findElement(By.name("search"));
        searchField.sendKeys("Ноутбук");
        String actValue = searchField.getAttribute("value");
        Assert.assertEquals(actValue, "Ноутбук", "Значення в полі пошуку не співпадає з очікуваним");
    }

    @Test
    public void testxPath() {
        //      Знаходження елемента за допомогою непрямого XPath
        WebElement cartButton = driver.findElement(By.xpath("//rz-main-header[contains(@class, 'header')]"));
        Assert.assertTrue(cartButton.isDisplayed(), "Навігація не відображається");
    }

    @Test
    public void testCLick() {
        //  	Клік по елементу
        WebElement laptopLink = driver.findElement(By.xpath("/html/body/rz-app-root/div/div/rz-main-page/div/aside/rz-main-page-sidebar/ul/li[1]/a"));
        Assert.assertNotNull(laptopLink);
        String linkText = laptopLink.getText();
        laptopLink.click();
        Assert.assertNotEquals(driver.getCurrentUrl(), url);

        //      Перевірка відповідності заголовка сторінки та назви розділу на сторінці
        WebElement h1 = driver.findElement(By.xpath("/html/body/rz-app-root/div/div/rz-super-portal/div/main/section/div[1]/h1"));
        String h1Text = h1.getText();
        Assert.assertEquals(h1Text.trim(), linkText.trim(), "Назва посилання та заголовок на сторінці після переходу за посиланням відрізняється");
    }

    @AfterEach
    public void teardown() {
        driver.quit();
    }
}