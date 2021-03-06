package com.sdl.webapp.common.util;

import com.google.common.base.Function;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class NestedCustomMapTest {

    public static final Function<Map.Entry<String, Map<String, Object>>, Map<String, Object>> SIMPLE_FUNCTION =
            new Function<Map.Entry<String, Map<String, Object>>, Map<String, Object>>() {
        @Override
        public Map<String, Object> apply(Map.Entry<String, Map<String, Object>> input) {
            Map<String, Object> map = input.getValue();
            String key = input.getKey();
            //noinspection unchecked
            return (Map<String, Object>) map.get(key);
        }
    };

    @Test
    public void shouldGetValueFromNestedMaps() throws Exception {
        //given
        final String expected = "Value";
        final Map<String, Object> map = new HashMap<>();
        final HashMap<String, Object> map2 = new HashMap<>();
        final HashMap<String, Object> map3 = new HashMap<>();
        map.put("Level1", map2);
        map2.put("Level2", map3);
        map3.put("Level3", expected);

        //when
        final String result = (String) new NestedCustomMap(map, SIMPLE_FUNCTION).get("Level1/Level2/Level3");

        //then
        assertEquals(expected, result);
    }

    @Test
    public void shouldGetValueFromFirstLevel() throws Exception {
        //given
        final String expected = "Value";
        final Map<String, Object> map = new HashMap<>();
        map.put("Level1", expected);

        //when
        final String result = (String) new NestedCustomMap(map, SIMPLE_FUNCTION).get("Level1");

        //then
        assertEquals(expected, result);
    }

    @Test
    public void shouldRegurnNullForWrongKey() throws Exception {
        //given
        final Map<String, Object> map = new HashMap<>();
        //when
        final String result = (String) new NestedCustomMap(map, SIMPLE_FUNCTION).get("Level1");

        //then
        assertNull(result);
    }
}