/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 Segment.io, Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.example.kotlin_sample

import android.app.Application
import android.util.Log
import android.widget.Toast
import com.snapyr.analytics.Analytics
import com.snapyr.analytics.Middleware
import com.snapyr.analytics.ValueMap
import com.snapyr.analytics.integrations.BasePayload
import com.snapyr.analytics.integrations.TrackPayload
import io.github.inflationx.calligraphy3.CalligraphyConfig
import io.github.inflationx.calligraphy3.CalligraphyInterceptor
import io.github.inflationx.viewpump.ViewPump

class SampleApp : Application() {

    private val ANALYTICS_WRITE_KEY: String = "your write key"
    override fun onCreate() {
        super.onCreate()

        ViewPump.init(
            ViewPump.builder()
                .addInterceptor(
                    CalligraphyInterceptor(
                        CalligraphyConfig.Builder()
                            .setDefaultFontPath("fonts/CircularStd-Book.otf")
                            .setFontAttrId(R.attr.fontPath)
                            .build()
                    )
                )
                .build()
        )

        // Initialize a new instance of the Analytics client.
        val builder = Analytics.Builder(this, ANALYTICS_WRITE_KEY)
            .experimentalNanosecondTimestamps()
            .trackApplicationLifecycleEvents()
            .logLevel(Analytics.LogLevel.DEBUG)
            .defaultProjectSettings(
                ValueMap()
                    .putValue(
                        "integrations",
                        ValueMap()
                            .putValue(
                                "adjust",
                                ValueMap()
                                    .putValue("appToken", "<>")
                                    .putValue(
                                        "trackAttributionData",
                                        true
                                    )
                            )
                    )
            )
            .useSourceMiddleware(
                    Middleware { chain ->
                        if (chain.payload().type() == BasePayload.Type.track) {
                            val payload = chain.payload() as TrackPayload
                            if (payload.event()
                                            .equals("Button B Clicked", ignoreCase = true)
                            ) {
                                chain.proceed(payload.toBuilder().build())
                                return@Middleware
                            }
                        }
                        chain.proceed(chain.payload())
                    }
            )
            .useDestinationMiddleware(
                "Snapyr",
                    Middleware { chain ->
                        if (chain.payload().type() == BasePayload.Type.track) {
                            val payload = chain.payload() as TrackPayload
                            if (payload.event()
                                            .equals("Button B Clicked", ignoreCase = true)
                            ) {
                                chain.proceed(payload.toBuilder().build())
                                return@Middleware
                            }
                        }
                        chain.proceed(chain.payload())
                    }
            )
            .flushQueueSize(1)
            .recordScreenViews()
            .actionHandler { Toast.makeText(this, "Action received: ${it.action}", Toast.LENGTH_SHORT).show() }
            .build()

        Analytics.setSingletonInstance(builder)

        val analytics = Analytics.with(this)

        analytics.onIntegrationReady(
            "Snapyr",
            Analytics.Callback<Any?> {
                Log.d("Snapyr Sample", "Snapyr integration ready.")
            }
        )
    }
}
