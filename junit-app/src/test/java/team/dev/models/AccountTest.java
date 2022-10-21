package team.dev.models;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.*;

import org.junit.jupiter.api.condition.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import team.dev.exceptions.MoneyInsuficientException;

//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AccountTest {
    Account cuenta;

    private TestInfo testInfo;
    private TestReporter testReporter;

    @BeforeEach
    void initMetodoTest(TestInfo testInfo, TestReporter testReporter) {
        this.cuenta = new Account("Andres", new BigDecimal("1000.12345"));
        this.testInfo = testInfo;
        this.testReporter = testReporter;
        System.out.println("iniciando el metodo.");
        testReporter.publishEntry(" ejecutando: " + testInfo.getDisplayName() + " " + testInfo.getTestMethod().orElse(null).getName()
                + " con las etiquetas " + testInfo.getTags());
    }

    @AfterEach
    void tearDown() {
        System.out.println("finalizando el metodo de prueba.");
    }

    @BeforeAll
    static void beforeAll() {
        System.out.println("inicializando el test");
    }

    @AfterAll
    static void afterAll() {
        System.out.println("finalizando el test");
    }

    @Tag("cuenta")
    @Nested
    @DisplayName("probando atributos de la cuenta corriente")
    class CuentaTestNombreSaldo{
        @Test
        @DisplayName("el nombre!")
        void testNombreCuenta() {
            testReporter.publishEntry(testInfo.getTags().toString());
            if (testInfo.getTags().contains("cuenta")) {
                testReporter.publishEntry("hacer algo con la etiqueta cuenta");
            }
//        cuenta.setPersona("Andres");
            String esperado = "Andres";
            String real = cuenta.getPerson();
            assertNotNull(real, () -> "la cuenta no puede ser nula");
            assertEquals(esperado, real, () -> "el nombre de la cuenta no es el que se esperaba: se esperaba " + esperado
                    + " sin embargo fue " + real);
            assertTrue(real.equals("Andres"), () -> "nombre cuenta esperada debe ser igual a la real");
        }

        @Test
        @DisplayName("el saldo, que no sea null, mayor que cero, valor esperado.")
        void testSaldoCuenta() {
            assertNotNull(cuenta.getBalance());
            assertEquals(1000.12345, cuenta.getBalance().doubleValue());
            assertFalse(cuenta.getBalance().compareTo(BigDecimal.ZERO) < 0);
            assertTrue(cuenta.getBalance().compareTo(BigDecimal.ZERO) > 0);
        }

        @Test
        @DisplayName("testeando referencias que sean iguales con el método equals.")
        void testReferenciaCuenta() {
            cuenta = new Account("John Doe", new BigDecimal("8900.9997"));
            Account cuenta2 = new Account("John Doe", new BigDecimal("8900.9997"));

//        assertNotEquals(cuenta2, cuenta);
            assertEquals(cuenta2, cuenta);

        }

    }

    @Nested
    class CuentaOperacionesTest{
        @Tag("cuenta")
        @Test
        void testDebitoCuenta() {
            cuenta.debit(new BigDecimal(100));
            assertNotNull(cuenta.getBalance());
            assertEquals(900, cuenta.getBalance().intValue());
            assertEquals("900.12345", cuenta.getBalance().toPlainString());
        }

        @Tag("cuenta")
        @Test
        void testCreditoCuenta() {
            cuenta.credit(new BigDecimal(100));
            assertNotNull(cuenta.getBalance());
            assertEquals(1100, cuenta.getBalance().intValue());
            assertEquals("1100.12345", cuenta.getBalance().toPlainString());
        }

        @Tag("cuenta")
        @Tag("banco")
        @Test
        void testTransferirDineroCuentas() {
            Account cuenta1 = new Account("Jhon Doe", new BigDecimal("2500"));
            Account cuenta2 = new Account("Andres", new BigDecimal("1500.8989"));

            Bank banco = new Bank();
            banco.setName("Banco del Estado");
            banco.transference(cuenta2, cuenta1, new BigDecimal(500));
            assertEquals("1000.8989", cuenta2.getBalance().toPlainString());
            assertEquals("3000", cuenta1.getBalance().toPlainString());
        }
    }


    @Test
    @Tag("cuenta")
    @Tag("error")
    void testDineroInsuficienteExceptionCuenta() {
        Exception exception = assertThrows(MoneyInsuficientException.class, () -> {
            cuenta.debit(new BigDecimal(1500));
        });
        String actual = exception.getMessage();
        String esperado = "Dinero Insuficiente";
        assertEquals(esperado, actual);
    }

    @Test
    @Tag("cuenta")
    @Tag("banco")
    // @Disabled
    @DisplayName("probando relaciones entre las cuentas y el banco con assertAll.")
    void testRelacionBancoCuentas() {
        //fail();
        Account cuenta1 = new Account("Jhon Doe", new BigDecimal("2500"));
        Account cuenta2 = new Account("Andres", new BigDecimal("1500.8989"));

        Bank banco = new Bank();
        banco.addAccount(cuenta1);
        banco.addAccount(cuenta2);

        banco.setName("Banco del Estado");
        banco.transference(cuenta2, cuenta1, new BigDecimal(500));
        assertAll(() -> assertEquals("1000.8989", cuenta2.getBalance().toPlainString(),
                        () -> "el valor del saldo de la cuenta2 no es el esperado."),
                () -> assertEquals("3000", cuenta1.getBalance().toPlainString(),
                        () -> "el valor del saldo de la cuenta1 no es el esperado."),
                () -> assertEquals(2, banco.getAccounts().size(), () -> "el banco no tienes las cuentas esperadas"),
                () -> assertEquals("Banco del Estado", cuenta1.getBank().getName()),
                () -> assertEquals("Andres", banco.getAccounts().stream()
                        .filter(c -> c.getPerson().equals("Andres"))
                        .findFirst()
                        .get().getPerson()),
                () -> assertTrue(banco.getAccounts().stream()
                        .anyMatch(c -> c.getPerson().equals("Jhon Doe"))));
    }

    @Nested
    class SistemaOperativoTest {
        @Test
        @EnabledOnOs(OS.WINDOWS)
        void testSoloWindows() {
        }

        @Test
        @EnabledOnOs({OS.LINUX, OS.MAC})
        void testSoloLinuxMac() {
        }

        @Test
        @DisabledOnOs(OS.WINDOWS)
        void testNoWindows() {
        }
    }

    @Nested
    class JavaVersionTest {
        @Test
        @EnabledOnJre(JRE.JAVA_8)
        void soloJdk8() {
        }

        @Test
        @EnabledOnJre(JRE.JAVA_15)
        void soloJDK15() {
        }

        @Test
        @DisabledOnJre(JRE.JAVA_15)
        void testNoJDK15() {
        }
    }

    @Nested
    class SistemPropertiesTest{
        @Test
        void imprimirSystemProperties() {
            Properties properties = System.getProperties();
            properties.forEach((k, v)-> System.out.println(k + ":" + v));
        }

        @Test
        @EnabledIfSystemProperty(named = "java.version", matches = ".*15.*")
        void testJavaVersion() {
        }

        @Test
        @DisabledIfSystemProperty(named = "os.arch", matches = ".*32.*")
        void testSolo64() {
        }

        @Test
        @EnabledIfSystemProperty(named = "os.arch", matches = ".*32.*")
        void testNO64() {
        }

        @Test
        @EnabledIfSystemProperty(named = "user.name", matches = "aguzm")
        void testUsername() {
        }

        @Test
        @EnabledIfSystemProperty(named = "ENV", matches = "dev")
        void testDev() {
        }
    }

    @Nested
    class VariableAmbienteTest{
        @Test
        void imprimirVariablesAmbiente() {
            Map<String, String> getenv = System.getenv();
            getenv.forEach((k, v)-> System.out.println(k + " = " + v));
        }

        @Test
        @EnabledIfEnvironmentVariable(named = "JAVA_HOME", matches = ".*jdk-15.0.1.*")
        void testJavaHome() {
        }

        @Test
        @EnabledIfEnvironmentVariable(named = "NUMBER_OF_PROCESSORS", matches = "8")
        void testProcesadores() {
        }

        @Test
        @EnabledIfEnvironmentVariable(named = "ENVIRONMENT", matches = "dev")
        void testEnv() {
        }

        @Test
        @DisabledIfEnvironmentVariable(named = "ENVIRONMENT", matches = "prod")
        void testEnvProdDisabled() {
        }

    }

    @Test
    @DisplayName("test Saldo Cuenta Dev")
    void testSaldoCuentaDev() {
        boolean esDev = "dev".equals(System.getProperty("ENV"));
        assumeTrue(esDev);
        assertNotNull(cuenta.getBalance());
        assertEquals(1000.12345, cuenta.getBalance().doubleValue());
        assertFalse(cuenta.getBalance().compareTo(BigDecimal.ZERO) < 0);
        assertTrue(cuenta.getBalance().compareTo(BigDecimal.ZERO) > 0);
    }

    @Test
    @DisplayName("test Saldo Cuenta Dev 2")
    void testSaldoCuentaDev2() {
        boolean esDev = "dev".equals(System.getProperty("ENV"));
        assumingThat(esDev, () -> {
            assertNotNull(cuenta.getBalance());
            assertEquals(1000.12345, cuenta.getBalance().doubleValue());
        });
        assertFalse(cuenta.getBalance().compareTo(BigDecimal.ZERO) < 0);
        assertTrue(cuenta.getBalance().compareTo(BigDecimal.ZERO) > 0);
    }

    @DisplayName("Probando Debito Cuenta Repetir!")
    @RepeatedTest(value=5, name = "{displayName} - Repetición numero {currentRepetition} de {totalRepetitions}")
    void testDebitoCuentaRepetir(RepetitionInfo info) {
        if(info.getCurrentRepetition() == 3){
            System.out.println("estamos en la repeticion " + info.getCurrentRepetition());
        }
        cuenta.debit(new BigDecimal(100));
        assertNotNull(cuenta.getBalance());
        assertEquals(900, cuenta.getBalance().intValue());
        assertEquals("900.12345", cuenta.getBalance().toPlainString());
    }

    @Tag("param")
    @Nested
    class PruebasParametrizadasTest{

        @ParameterizedTest(name = "numero {index} ejecutando con valor {0} - {argumentsWithNames}")
        @ValueSource(strings = {"100", "200", "300", "500", "700", "1000.12345"})
        void testDebitoCuentaValueSource(String monto) {
            cuenta.debit(new BigDecimal(monto));
            assertNotNull(cuenta.getBalance());
            assertTrue(cuenta.getBalance().compareTo(BigDecimal.ZERO) > 0);
        }

        @ParameterizedTest(name = "numero {index} ejecutando con valor {0} - {argumentsWithNames}")
        @CsvSource({"1,100", "2,200", "3,300", "4,500", "5,700", "6,1000.12345"})
        void testDebitoCuentaCsvSource(String index, String monto) {
            System.out.println(index + " -> " + monto);
            cuenta.debit(new BigDecimal(monto));
            assertNotNull(cuenta.getBalance());
            assertTrue(cuenta.getBalance().compareTo(BigDecimal.ZERO) > 0);
        }

        @ParameterizedTest(name = "numero {index} ejecutando con valor {0} - {argumentsWithNames}")
        @CsvSource({"200,100,John,Andres", "250,200,Pepe,Pepe", "300,300,maria,Maria", "510,500,Pepa,Pepa", "750,700,Lucas,Luca", "1000.12345,1000.12345,Cata,Cata"})
        void testDebitoCuentaCsvSource2(String saldo, String monto, String esperado, String actual) {
            System.out.println(saldo + " -> " + monto);
            cuenta.setBalance(new BigDecimal(saldo));
            cuenta.debit(new BigDecimal(monto));
            cuenta.setPerson(actual);

            assertNotNull(cuenta.getBalance());
            assertNotNull(cuenta.getPerson());
            assertEquals(esperado, actual);
            assertTrue(cuenta.getBalance().compareTo(BigDecimal.ZERO) > 0);
        }

        @ParameterizedTest(name = "numero {index} ejecutando con valor {0} - {argumentsWithNames}")
        @CsvFileSource(resources = "/data.csv")
        void testDebitoCuentaCsvFileSource(String monto) {
            cuenta.debit(new BigDecimal(monto));
            assertNotNull(cuenta.getBalance());
            assertTrue(cuenta.getBalance().compareTo(BigDecimal.ZERO) > 0);
        }

        @ParameterizedTest(name = "numero {index} ejecutando con valor {0} - {argumentsWithNames}")
        @CsvFileSource(resources = "/data2.csv")
        void testDebitoCuentaCsvFileSource2(String saldo, String monto, String esperado, String actual) {
            cuenta.setBalance(new BigDecimal(saldo));
            cuenta.debit(new BigDecimal(monto));
            cuenta.setPerson(actual);

            assertNotNull(cuenta.getBalance());
            assertNotNull(cuenta.getPerson());
            assertEquals(esperado, actual);

            assertTrue(cuenta.getBalance().compareTo(BigDecimal.ZERO) > 0);
        }

    }

    @Tag("param")
    @ParameterizedTest(name = "numero {index} ejecutando con valor {0} - {argumentsWithNames}")
    @MethodSource("montoList")
    void testDebitoCuentaMethodSource(String monto) {
        cuenta.debit(new BigDecimal(monto));
        assertNotNull(cuenta.getBalance());
        assertTrue(cuenta.getBalance().compareTo(BigDecimal.ZERO) > 0);
    }

    static List<String> montoList() {
        return Arrays.asList("100", "200", "300", "500", "700", "1000.12345");
    }

    @Nested
    @Tag("timeout")
    class EjemploTimeoutTest{
        @Test
        @Timeout(1)
        void pruebaTimeout() throws InterruptedException {
            TimeUnit.MILLISECONDS.sleep(100);
        }

        @Test
        @Timeout(value = 1000, unit = TimeUnit.MILLISECONDS)
        void pruebaTimeout2() throws InterruptedException {
            TimeUnit.MILLISECONDS.sleep(900);
        }

        @Test
        void testTimeoutAssertions() {
            assertTimeout(Duration.ofSeconds(5), ()->{
                TimeUnit.MILLISECONDS.sleep(4000);
            });
        }
    }
}