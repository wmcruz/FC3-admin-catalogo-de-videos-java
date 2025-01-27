package com.fullcycle.admin.catalogo.domain.video;

import com.fullcycle.admin.catalogo.domain.castmember.CastMemberID;
import com.fullcycle.admin.catalogo.domain.category.CategoryID;
import com.fullcycle.admin.catalogo.domain.exceptions.DomainException;
import com.fullcycle.admin.catalogo.domain.genre.GenreID;
import com.fullcycle.admin.catalogo.domain.validation.handler.ThrowsValidationHandler;
import org.junit.jupiter.api.Test;

import java.time.Year;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class VideoValidatorTest {

    @Test
    public void givenNullTitle_whenCallsValidate_shouldReceiveError() {
        // given
        final String expectedTitle = null;
        final var expectedDescription = """
                Essa é uma descrição de teste para esse vídeo.""";
        final var expectedLaunchedAt = Year.of(2022);
        final var expectedDuration = 120.10;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedRating = Rating.L;
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedMembers = Set.of(CastMemberID.unique());

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'title' should not be null";

        final var actualVideo = Video.newVideo(
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt,
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating,
                expectedCategories,
                expectedGenres,
                expectedMembers
        );

        // when
        final var actualError = assertThrows(DomainException.class, () -> actualVideo.validate(new ThrowsValidationHandler()));

        // then
        assertEquals(expectedErrorCount, actualError.getErrors().size());
        assertEquals(expectedErrorMessage, actualError.getErrors().get(0).message());
    }

    @Test
    public void givenEmptyTitle_whenCallsValidate_shouldReceiveError() {
        // given
        final var expectedTitle = "";
        final var expectedDescription = """
                Essa é uma descrição de teste para esse vídeo.""";
        final var expectedLaunchedAt = Year.of(2022);
        final var expectedDuration = 120.10;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedRating = Rating.L;
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedMembers = Set.of(CastMemberID.unique());

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'title' should not be empty";

        final var actualVideo = Video.newVideo(
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt,
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating,
                expectedCategories,
                expectedGenres,
                expectedMembers
        );

        // when
        final var actualError = assertThrows(DomainException.class, () -> actualVideo.validate(new ThrowsValidationHandler()));

        // then
        assertEquals(expectedErrorCount, actualError.getErrors().size());
        assertEquals(expectedErrorMessage, actualError.getErrors().get(0).message());
    }

    @Test
    public void givenTitleWithLengthGreaterThan255_whenCallsValidate_shouldReceiveError() {
        // given
        final var expectedTitle = "" +
                " A prática cotidiana prova que a execução dos pontos do programa nos obriga à análise dos procedimentos normalmente adotados. É claro que o novo modelo estrutural aqui preconizado oferece uma interessante oportunidade para verificação de todos os recursos funcionais envolvidos. A nível organizacional, o julgamento imparcial das eventualidades exige a precisão e a definição do sistema de participação geral. No entanto, não podemos esquecer que o desafiador cenário globalizado afeta positivamente a correta previsão dos paradigmas corporativos. Do mesmo modo, a consolidação das estruturas garante a contribuição de um grupo importante na determinação das novas proposições." +
                "          O incentivo ao avanço tecnológico, assim como o desenvolvimento contínuo de distintas formas de atuação acarreta um processo de reformulação e modernização do sistema de formação de quadros que corresponde às necessidades. Caros amigos, a constante divulgação das informações facilita a criação do levantamento das variáveis envolvidas. Nunca é demais lembrar o peso e o significado destes problemas, uma vez que a percepção das dificuldades prepara-nos para enfrentar situações atípicas decorrentes dos métodos utilizados na avaliação de resultados.";
        final var expectedDescription = """
                Essa é uma descrição de teste para esse vídeo.""";
        final var expectedLaunchedAt = Year.of(2022);
        final var expectedDuration = 120.10;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedRating = Rating.L;
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedMembers = Set.of(CastMemberID.unique());

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'title' must be between 1 and 255 characters";

        final var actualVideo = Video.newVideo(
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt,
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating,
                expectedCategories,
                expectedGenres,
                expectedMembers
        );

        // when
        final var actualError = assertThrows(DomainException.class, () -> actualVideo.validate(new ThrowsValidationHandler()));

        // then
        assertEquals(expectedErrorCount, actualError.getErrors().size());
        assertEquals(expectedErrorMessage, actualError.getErrors().get(0).message());
    }

    @Test
    public void givenEmptyDescription_whenCallsValidate_shouldReceiveError() {
        // given
        final var expectedTitle = "System Design Interviews";
        final var expectedDescription = "";
        final var expectedLaunchedAt = Year.of(2022);
        final var expectedDuration = 120.10;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedRating = Rating.L;
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedMembers = Set.of(CastMemberID.unique());

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'description' should not be empty";

        final var actualVideo = Video.newVideo(
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt,
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating,
                expectedCategories,
                expectedGenres,
                expectedMembers
        );

        // when
        final var actualError = assertThrows(DomainException.class, () -> actualVideo.validate(new ThrowsValidationHandler()));

        // then
        assertEquals(expectedErrorCount, actualError.getErrors().size());
        assertEquals(expectedErrorMessage, actualError.getErrors().get(0).message());
    }

    @Test
    public void givenDescriptionWithLengthGreaterThan4000_whenCallsValidate_shouldReceiveError() {
        // given
        final var expectedTitle = "System Design Interviews";
        final var expectedDescription = "   " +
                "A prática cotidiana prova que a execução dos pontos do programa nos obriga à análise dos procedimentos normalmente adotados. É claro que o novo modelo estrutural aqui preconizado oferece uma interessante oportunidade para verificação de todos os recursos funcionais envolvidos. A nível organizacional, o julgamento imparcial das eventualidades exige a precisão e a definição do sistema de participação geral. No entanto, não podemos esquecer que o desafiador cenário globalizado afeta positivamente a correta previsão dos paradigmas corporativos. Do mesmo modo, a consolidação das estruturas garante a contribuição de um grupo importante na determinação das novas proposições." +
                "          O incentivo ao avanço tecnológico, assim como o desenvolvimento contínuo de distintas formas de atuação acarreta um processo de reformulação e modernização do sistema de formação de quadros que corresponde às necessidades. Caros amigos, a constante divulgação das informações facilita a criação do levantamento das variáveis envolvidas. Nunca é demais lembrar o peso e o significado destes problemas, uma vez que a percepção das dificuldades prepara-nos para enfrentar situações atípicas decorrentes dos métodos utilizados na avaliação de resultados." +
                "          Acima de tudo, é fundamental ressaltar que a expansão dos mercados mundiais obstaculiza a apreciação da importância das formas de ação. Por outro lado, o aumento do diálogo entre os diferentes setores produtivos deve passar por modificações independentemente do investimento em reciclagem técnica. A certificação de metodologias que nos auxiliam a lidar com a determinação clara de objetivos pode nos levar a considerar a reestruturação dos relacionamentos verticais entre as hierarquias. Percebemos, cada vez mais, que a mobilidade dos capitais internacionais estende o alcance e a importância do impacto na agilidade decisória." +
                "          Pensando mais a longo prazo, a adoção de políticas descentralizadoras é uma das consequências do orçamento setorial. Todavia, a necessidade de renovação processual talvez venha a ressaltar a relatividade das regras de conduta normativas. Ainda assim, existem dúvidas a respeito de como a valorização de fatores subjetivos promove a alavancagem das condições financeiras e administrativas exigidas. Gostaria de enfatizar que a hegemonia do ambiente político não pode mais se dissociar dos níveis de motivação departamental. No mundo atual, o surgimento do comércio virtual apresenta tendências no sentido de aprovar a manutenção do remanejamento dos quadros funcionais." +
                "          O empenho em analisar a revolução dos costumes agrega valor ao estabelecimento dos modos de operação convencionais. O que temos que ter sempre em mente é que a competitividade nas transações comerciais possibilita uma melhor visão global de alternativas às soluções ortodoxas. Todas estas questões, devidamente ponderadas, levantam dúvidas sobre se o acompanhamento das preferências de consumo cumpre um papel essencial na formulação das diretrizes de desenvolvimento para o futuro. Por conseguinte, o início da atividade geral de formação de atitudes auxilia a preparação e a composição dos conhecimentos estratégicos para atingir a excelência." +
                "          O cuidado em identificar pontos críticos na consulta aos diversos militantes assume importantes posições no estabelecimento das direções preferenciais no sentido do progresso. Assim mesmo, o fenômeno da Internet estimula a padronização das posturas dos órgãos dirigentes com relação às suas atribuições. Evidentemente, a estrutura atual da organização ainda não demonstrou convincentemente que vai participar na mudança do retorno esperado a longo prazo. Neste sentido, a complexidade dos estudos efetuados faz parte de um processo de gerenciamento das condições inegavelmente apropriadas." +
                "          O cuidado em identificar pontos críticos na consulta aos diversos militantes assume importantes posições no estabelecimento das direções preferenciais no sentido do progresso. Assim mesmo, o fenômeno da Internet estimula a padronização das posturas dos órgãos dirigentes com relação às suas atribuições. Evidentemente, a estrutura atual da organização ainda não demonstrou convincentemente que vai participar na mudança do retorno esperado a longo prazo. Neste sentido, a complexidade dos estudos efetuados faz parte de um processo de gerenciamento das condições inegavelmente apropriadas." +
                "A prática cotidiana prova que a execução dos pontos do programa nos obriga à análise dos procedimentos normalmente adotados. É claro que o novo modelo estrutural aqui preconizado oferece uma interessante oportunidade para verificação de todos os recursos funcionais envolvidos. A nível organizacional, o julgamento imparcial das eventualidades exige a precisão e a definição do sistema de participação geral. No entanto, não podemos esquecer que o desafiador cenário globalizado afeta positivamente a correta previsão dos paradigmas corporativos. Do mesmo modo, a consolidação das estruturas garante a contribuição de um grupo importante na determinação das novas proposições." +
                "          O incentivo ao avanço tecnológico, assim como o desenvolvimento contínuo de distintas formas de atuação acarreta um processo de reformulação e modernização do sistema de formação de quadros que corresponde às necessidades. Caros amigos, a constante divulgação das informações facilita a criação do levantamento das variáveis envolvidas. Nunca é demais lembrar o peso e o significado destes problemas, uma vez que a percepção das dificuldades prepara-nos para enfrentar situações atípicas decorrentes dos métodos utilizados na avaliação de resultados." +
                "          Acima de tudo, é fundamental ressaltar que a expansão dos mercados mundiais obstaculiza a apreciação da importância das formas de ação. Por outro lado, o aumento do diálogo entre os diferentes setores produtivos deve passar por modificações independentemente do investimento em reciclagem técnica. A certificação de metodologias que nos auxiliam a lidar com a determinação clara de objetivos pode nos levar a considerar a reestruturação dos relacionamentos verticais entre as hierarquias. Percebemos, cada vez mais, que a mobilidade dos capitais internacionais estende o alcance e a importância do impacto na agilidade decisória." +
                "          Pensando mais a longo prazo, a adoção de políticas descentralizadoras é uma das consequências do orçamento setorial. Todavia, a necessidade de renovação processual talvez venha a ressaltar a relatividade das regras de conduta normativas. Ainda assim, existem dúvidas a respeito de como a valorização de fatores subjetivos promove a alavancagem das condições financeiras e administrativas exigidas. Gostaria de enfatizar que a hegemonia do ambiente político não pode mais se dissociar dos níveis de motivação departamental. No mundo atual, o surgimento do comércio virtual apresenta tendências no sentido de aprovar a manutenção do remanejamento dos quadros funcionais." +
                "          O empenho em analisar a revolução dos costumes agrega valor ao estabelecimento dos modos de operação convencionais. O que temos que ter sempre em mente é que a competitividade nas transações comerciais possibilita uma melhor visão global de alternativas às soluções ortodoxas. Todas estas questões, devidamente ponderadas, levantam dúvidas sobre se o acompanhamento das preferências de consumo cumpre um papel essencial na formulação das diretrizes de desenvolvimento para o futuro. Por conseguinte, o início da atividade geral de formação de atitudes auxilia a preparação e a composição dos conhecimentos estratégicos para atingir a excelência." +
                "          O cuidado em identificar pontos críticos na consulta aos diversos militantes assume importantes posições no estabelecimento das direções preferenciais no sentido do progresso. Assim mesmo, o fenômeno da Internet estimula a padronização das posturas dos órgãos dirigentes com relação às suas atribuições. Evidentemente, a estrutura atual da organização ainda não demonstrou convincentemente que vai participar na mudança do retorno esperado a longo prazo. Neste sentido, a complexidade dos estudos efetuados faz parte de um processo de gerenciamento das condições inegavelmente apropriadas." +
                "          O cuidado em identificar pontos críticos na consulta aos diversos militantes assume importantes posições no estabelecimento das direções preferenciais no sentido do progresso. Assim mesmo, o fenômeno da Internet estimula a padronização das posturas dos órgãos dirigentes com relação às suas atribuições. Evidentemente, a estrutura atual da organização ainda não demonstrou convincentemente que vai participar na mudança do retorno esperado a longo prazo. Neste sentido, a complexidade dos estudos efetuados faz parte de um processo de gerenciamento das condições inegavelmente apropriadas.";
        final var expectedLaunchedAt = Year.of(2022);
        final var expectedDuration = 120.10;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedRating = Rating.L;
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedMembers = Set.of(CastMemberID.unique());

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'description' must be between 1 and 4000 characters";

        final var actualVideo = Video.newVideo(
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt,
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating,
                expectedCategories,
                expectedGenres,
                expectedMembers
        );

        // when
        final var actualError = assertThrows(DomainException.class, () -> actualVideo.validate(new ThrowsValidationHandler()));

        // then
        assertEquals(expectedErrorCount, actualError.getErrors().size());
        assertEquals(expectedErrorMessage, actualError.getErrors().get(0).message());
    }

    @Test
    public void givenNullLaunchedAt_whenCallsValidate_shouldReceiveError() {
        // given
        final var expectedTitle = "System Design Interviews";
        final var expectedDescription = """
                Essa é uma descrição de teste para esse vídeo.""";
        final Year expectedLaunchedAt = null;
        final var expectedDuration = 120.10;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedRating = Rating.L;
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedMembers = Set.of(CastMemberID.unique());

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'launchedAt' should not be null";

        final var actualVideo = Video.newVideo(
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt,
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating,
                expectedCategories,
                expectedGenres,
                expectedMembers
        );

        final var validator = new VideoValidator(actualVideo, new ThrowsValidationHandler());

        // when
        final var actualError = assertThrows(DomainException.class, () -> actualVideo.validate(new ThrowsValidationHandler()));

        // then
        assertEquals(expectedErrorCount, actualError.getErrors().size());
        assertEquals(expectedErrorMessage, actualError.getErrors().get(0).message());
    }

    @Test
    public void givenNullRating_whenCallsValidate_shouldReceiveError() {
        // given
        final var expectedTitle = "System Design Interviews";
        final var expectedDescription = """
                Essa é uma descrição de teste para esse vídeo.""";
        final var expectedLaunchedAt = Year.of(2022);
        final var expectedDuration = 120.10;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final Rating expectedRating = null;
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedMembers = Set.of(CastMemberID.unique());

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'rating' should not be null";

        final var actualVideo = Video.newVideo(
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt,
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating,
                expectedCategories,
                expectedGenres,
                expectedMembers
        );

        // when
        final var actualError = assertThrows(DomainException.class, () -> actualVideo.validate(new ThrowsValidationHandler()));

        // then
        assertEquals(expectedErrorCount, actualError.getErrors().size());
        assertEquals(expectedErrorMessage, actualError.getErrors().get(0).message());
    }
}