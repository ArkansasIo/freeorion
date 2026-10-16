package org.arkansasio.freeorion

import org.arkansasio.freeorion.api.ApiConfig
import org.junit.Assert.assertTrue
import org.junit.Test

class ApiConfigTest {
    @Test
    fun baseUrlUsesHttpAddressWithTrailingSlash() {
        assertTrue(ApiConfig.BASE_URL.startsWith("http://") || ApiConfig.BASE_URL.startsWith("https://"))
        assertTrue(ApiConfig.BASE_URL.endsWith("/"))
    }
}
