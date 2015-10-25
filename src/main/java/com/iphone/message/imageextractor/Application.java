package com.iphone.message.imageextractor;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.sqlite.SQLiteDataSource;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

public class Application {
    private static final String QUERY = "SELECT filename FROM attachment WHERE mime_type = 'image/jpeg';";
    private static final String MESSAGE_DB_FILE_NAME = "3d0d7e5fb2ce288813306e4d4636395e047a3d28";

    public static void main(String[] args) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(createDataSource());
        getImagePaths(jdbcTemplate).forEach(copyFiles);
    }

    static DataSource createDataSource() {
        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl("jdbc:sqlite:" + MESSAGE_DB_FILE_NAME);
        return dataSource;
    }

    static Stream<String> getImagePaths(JdbcTemplate jdbcTemplate) {
        List<String> imagePaths = jdbcTemplate.queryForList(QUERY, String.class);
        System.out.println("Found " + imagePaths.size() + " images");
        return imagePaths.stream();
    }

    static Function<String, Path> toPath = Paths::get;
    static UnaryOperator<String> adjustPath = s -> s.replaceFirst("~/", "MediaDomain-");
    static UnaryOperator<String> toSha1 = DigestUtils::sha1Hex;
    static UnaryOperator<String> extractFileName = s -> s.substring(s.lastIndexOf("/") + 1);

    static Consumer<String> copyFiles = s -> {
        try {
            Path sha1 = adjustPath.andThen(toSha1).andThen(toPath).apply(s);
            Path fileName = toPath.compose(extractFileName).apply(s);
            System.out.println("copy " + sha1 + " to " + fileName);
            Files.copy(sha1, fileName, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    };
}
