package bootwildfly;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWildFlyController {


    @RequestMapping("hello")
    public String sayHello(){
        return ("Hello, SpringBoot on Wildfly");
    }

    @RequestMapping("uitest")
    public String startSelenium(){
        try {
            WebDriver driver = new HtmlUnitDriver();
            driver.get("http://google.de");
            return driver.getTitle();
        }catch (Exception e){
            return "Fail!";
        }
    }
}