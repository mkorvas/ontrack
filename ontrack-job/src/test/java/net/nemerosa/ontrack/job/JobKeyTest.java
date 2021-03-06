package net.nemerosa.ontrack.job;

import net.nemerosa.ontrack.job.support.CountJob;
import net.nemerosa.ontrack.job.support.LongCountJob;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class JobKeyTest {

    @Test
    public void same_category() {
        JobKey key = Fixtures.TEST_CATEGORY.getType("xxx").getKey("xxx");
        assertTrue(key.sameCategory(Fixtures.TEST_CATEGORY));
        assertTrue(new CountJob().getKey().sameCategory(Fixtures.TEST_CATEGORY));
        assertTrue(new LongCountJob().getKey().sameCategory(Fixtures.TEST_CATEGORY));
    }

    @Test
    public void key_string() {
        JobKey key = Fixtures.TEST_CATEGORY.getType("my-type").getKey("my-key");
        assertEquals("[test][my-type][my-key]", key.toString());
    }

}
