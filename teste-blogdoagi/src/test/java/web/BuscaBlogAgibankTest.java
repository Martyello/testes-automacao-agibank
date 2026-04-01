package web;

import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import static org.junit.jupiter.api.Assertions.*;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;
import java.util.List;
import org.openqa.selenium.chrome.ChromeOptions;
import io.github.bonigarcia.wdm.WebDriverManager;

public class BuscaBlogAgibankTest {

    private WebDriver browser;
    private WebDriverWait driverWait;
    private final String BASE_URL = "https://blog.agibank.com.br/";

    @BeforeAll
    static void init() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setUp() {

        ChromeOptions options = new ChromeOptions();

        options.addArguments("--headless");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");

        browser = new ChromeDriver(options);

        driverWait = new WebDriverWait(browser, Duration.ofSeconds(6));
    }

    @AfterEach
    void tearDown() {
        if (browser != null) browser.quit();
    }

    @Test
    @DisplayName("Validar busca funcional com termos genéricos")
    public void validarBuscaComSucesso() {
        var key = "finanças";

        browser.get(BASE_URL + "?s=" + key);

        // Aguarda carregar os blocos de artigos antes de validar
        driverWait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.tagName("article")));

        var html = browser.getPageSource().toLowerCase();
        assertTrue(html.contains(key), "O termo buscado deveria constar no source da página");
    }

    @Test
    public void testarBuscaVazia() {

        browser.get(BASE_URL + "?s=");

        driverWait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));

        String pagina = browser.getPageSource().toLowerCase();

        // valida que a página carregou corretamente
        assertNotNull(pagina);

        // valida que existe alguma mensagem na tela
        boolean temFeedback = pagina.contains("resultado") || pagina.contains("nenhum");
        assertTrue(temFeedback, "A página deveria exibir algum feedback para busca vazia.");
    }

    @Test
    public void validarInputInvalido() {
        var dummyTerm = "@#$%";
        browser.get(BASE_URL + "?s=" + dummyTerm);

        // Aqui o foco é garantir resiliência do front-end
        driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("primary")));

        String content = browser.getPageSource().toLowerCase();

        // Verifica se a mensagem amigável de 'não encontrado' aparece
        boolean achouMensagem = content.contains("resultados") || content.contains("nenhum");
        assertTrue(achouMensagem, "Deveria exibir feedback de busca sem sucesso.");
    }

    @Test
    public void buscarTermoInexistente() {

        String weirdQuery = "Parafusos99x";

        browser.get(BASE_URL + "?s=" + weirdQuery);
        driverWait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("body")));

        var source = browser.getPageSource().toLowerCase();

        assertAll("Validando feedback de busca inexistente",
                () -> assertNotNull(source),
                () -> assertTrue(source.contains("resultado"), "Página deve renderizar área de busca.")
        );
    }
}