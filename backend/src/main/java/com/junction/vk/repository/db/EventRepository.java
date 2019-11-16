package com.junction.vk.repository.db;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;
import com.junction.vk.domain.Feed;
import com.junction.vk.repository.db.base.AbstractRepository;

@Repository
public class EventRepository extends AbstractRepository {
    private static final Logger logger = LoggerFactory.getLogger(EventRepository.class);

    private static final String SQL_INSERT_FEED = "";

    private static final String SQL_SELECT_ALL_FEED = "";

    public EventRepository(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    public boolean setFeed(long userId, long productId, boolean isLicked) {
        try {
            MapSqlParameterSource namedParameters = new MapSqlParameterSource();
            namedParameters.addValue("userId", userId);
            namedParameters.addValue("productId", productId);
            namedParameters.addValue("isLicked", isLicked);

            if (npjtTemplate.update(SQL_INSERT_FEED, namedParameters) > 0) {
                return true;
            }
            logger.error("Can't insert feed data, user id {}.", userId);
        } catch (DataAccessException ex) {
            logger.error("Invoke setFeed({}, {}, {}) with exception.", userId, productId, isLicked);
        }
        return false;
    }

    public Map<Long, List<Feed>> getUsersFeed() {
        try {
            List<Feed> feeds = npjtTemplate.query(SQL_SELECT_ALL_FEED, getFeedRowMapper());

            return feeds.stream().collect(Collectors.groupingBy(Feed::getUserId));
        } catch (DataAccessException ex) {
            logger.error("Invoke getUsersFeed() with exception.", ex);
        }
        return Collections.emptyMap();
    }

    private static RowMapper<Feed> getFeedRowMapper() {
        return (rs, i) -> new Feed(
                rs.getLong(""),
                rs.getLong(""),
                rs.getBoolean("")
        );
    }
}
