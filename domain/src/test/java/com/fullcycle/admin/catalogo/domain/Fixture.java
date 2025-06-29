package com.fullcycle.admin.catalogo.domain;

import com.fullcycle.admin.catalogo.domain.castmember.CastMember;
import com.fullcycle.admin.catalogo.domain.castmember.CastMemberType;
import com.fullcycle.admin.catalogo.domain.category.Category;
import com.fullcycle.admin.catalogo.domain.genre.Genre;
import com.fullcycle.admin.catalogo.domain.resource.Resource;
import com.fullcycle.admin.catalogo.domain.utils.IdUtils;
import com.fullcycle.admin.catalogo.domain.video.AudioVideoMedia;
import com.fullcycle.admin.catalogo.domain.video.ImageMedia;
import com.fullcycle.admin.catalogo.domain.video.Rating;
import com.fullcycle.admin.catalogo.domain.video.Video;
import com.fullcycle.admin.catalogo.domain.video.VideoMediaType;
import com.github.javafaker.Faker;

import java.time.Year;
import java.util.Set;

import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.API.List;
import static io.vavr.API.Match;

public final class Fixture {

    private static final Faker FAKER = new Faker();

    public static String name() {
        return FAKER.name().fullName();
    }

    public static String title() {
        return FAKER.options().option(
                "Title 1",
                "Title 2",
                "Title 3"
        );
    }

    public static String checksum() {
        return "03fe62de";
    }

    public static Video video() {
        return Video.newVideo(
                Fixture.title(),
                Videos.description(),
                Year.of(Fixture.year()),
                Fixture.duration(),
                Fixture.bool(),
                Fixture.bool(),
                Videos.rating(),
                Set.of(Categories.aulas().getId()),
                Set.of(Genres.tech().getId()),
                Set.of(CastMembers.wesley().getId(), CastMembers.wellington().getId())
        );
    }

    public static Integer year() {
        return FAKER.random().nextInt(2020, 2030);
    }

    public static Double duration() {
        return FAKER.options().option(120.0, 15.5, 35.5, 10.0, 2.0);
    }

    public static boolean bool() {
        return FAKER.bool().bool();
    }

    public static final class Categories {

        private static final Category AULAS = Category.newCategory("Aulas", "Some description", true);
        private static final Category LIVES = Category.newCategory("Lives", "Some description", true);

        public static Category aulas() {
            return AULAS.clone();
        }

        public static Category lives() {
            return LIVES.clone();
        }
    }

    public static final class Genres {

        private static final Genre TECH = Genre.newGenre("Technology", true);
        private static final Genre BUSINESS = Genre.newGenre("Business", true);

        public static Genre tech() {
            return Genre.with(TECH);
        }

        public static Genre business() {
            return Genre.with(BUSINESS);
        }
    }

    public static final class CastMembers {

        private static final CastMember WESLEY = CastMember.newMember("Wesley FullCycle", CastMemberType.ACTOR);
        private static final CastMember WELLINGTON = CastMember.newMember("Wellington Ton", CastMemberType.ACTOR);

        public static CastMemberType type() {
            return FAKER.options().option(CastMemberType.values());
        }

        public static CastMember wesley() {
            return CastMember.with(WESLEY);
        }

        public static CastMember wellington() {
            return CastMember.with(WELLINGTON);
        }
    }

    public static final class Videos {

        public static Video systemDesign() {
            return Video.newVideo(
                    Fixture.title(),
                    description(),
                    Year.of(Fixture.year()),
                    Fixture.duration(),
                    Fixture.bool(),
                    Fixture.bool(),
                    rating(),
                    Set.of(Categories.aulas().getId()),
                    Set.of(Genres.tech().getId()),
                    Set.of(CastMembers.wesley().getId(), CastMembers.wellington().getId())
            );
        }

        public static Rating rating() {
            return FAKER.options().option(Rating.values());
        }

        public static VideoMediaType mediaType() {
            return FAKER.options().option(VideoMediaType.values());
        }

        public static Resource resource(final VideoMediaType type) {
            final String contentType = Match(type).of(
                    Case($(List(VideoMediaType.VIDEO, VideoMediaType.TRAILER)::contains), "video/mp4"),
                    Case($(), "image/png")
            );

            final String checksum = IdUtils.uuid();
            final byte[] content = "Conteudo".getBytes();

            return Resource.with(checksum, content, contentType, type.name().toLowerCase());
        }

        public static String description() {
            return FAKER.options().option(
                    " Caros amigos, a hegemonia do ambiente político facilita a criação do retorno esperado a longo prazo. A nível organizacional, o julgamento imparcial das eventualidades representa uma abertura para a melhoria das diretrizes de desenvolvimento para o futuro. Assim mesmo, a contínua expansão de nossa atividade exige a precisão e a definição do sistema de participação geral. Neste sentido, a consulta aos diversos militantes assume importantes posições no estabelecimento da gestão inovadora da qual fazemos parte.",
                    "Não obstante, o novo modelo estrutural aqui preconizado garante a contribuição de um grupo importante na determinação das novas proposições. A prática cotidiana prova que a valorização de fatores subjetivos apresenta tendências no sentido de aprovar a manutenção das direções preferenciais no sentido do progresso. Nunca é demais lembrar o peso e o significado destes problemas, uma vez que a constante divulgação das informações deve passar por modificações independentemente dos índices pretendidos. O incentivo ao avanço tecnológico, assim como a competitividade nas transações comerciais oferece uma interessante oportunidade para verificação das formas de ação."
            );
        }

        public static AudioVideoMedia audioVideo(final VideoMediaType type) {
            final var checksum = Fixture.checksum();
            return AudioVideoMedia.with(
                    checksum,
                    type.name().toLowerCase(),
                    "/videos/" + checksum
            );
        }

        public static ImageMedia image(final VideoMediaType type) {
            final var checksum = Fixture.checksum();
            return ImageMedia.with(
                    checksum,
                    type.name().toLowerCase(),
                    "/images/" + checksum
            );
        }
    }
}