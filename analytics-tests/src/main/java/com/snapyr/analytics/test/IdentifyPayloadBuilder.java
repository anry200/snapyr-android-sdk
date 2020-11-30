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
package com.snapyr.analytics.test;

import com.snapyr.analytics.AnalyticsContext;
import com.snapyr.analytics.Options;
import com.snapyr.analytics.Traits;
import com.snapyr.analytics.Utils;
import com.snapyr.analytics.integrations.IdentifyPayload;

@Deprecated
public class IdentifyPayloadBuilder {

    private AnalyticsContext context;
    private Traits traits;
    private Options options;

    public IdentifyPayloadBuilder traits(Traits traits) {
        this.traits = traits;
        return this;
    }

    public IdentifyPayloadBuilder options(Options options) {
        this.options = options;
        return this;
    }

    public IdentifyPayloadBuilder context(AnalyticsContext context) {
        this.context = context;
        return this;
    }

    public IdentifyPayload build() {
        if (traits == null) {
            traits = Utils.createTraits();
        }
        if (context == null) {
            context = Utils.createContext(traits);
        }
        if (options == null) {
            options = new Options();
        }

        return new IdentifyPayload.Builder()
                .userId(traits.userId())
                .traits(traits)
                .anonymousId(traits.anonymousId())
                .context(context)
                .integrations(options.integrations())
                .build();
    }
}
