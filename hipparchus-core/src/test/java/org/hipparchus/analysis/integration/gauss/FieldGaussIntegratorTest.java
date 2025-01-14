/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/*
 * This is not the original file distributed by the Apache Software Foundation
 * It has been modified by the Hipparchus project
 */
package org.hipparchus.analysis.integration.gauss;

import org.hipparchus.analysis.CalculusFieldUnivariateFunction;
import org.hipparchus.util.Decimal64;
import org.hipparchus.util.Pair;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test for {@link GaussIntegrator} class.
 *
 */
public class FieldGaussIntegratorTest {
    @Test
    public void testGetWeights() {
        final double[] points = { 0, 1.2, 3.4 };
        final double[] weights = { 9.8, 7.6, 5.4 };

        final FieldGaussIntegrator<Decimal64> integrator
            = new FieldGaussIntegrator<>(new Pair<>(toDecimal64(points), toDecimal64(weights)));

        Assert.assertEquals(weights.length, integrator.getNumberOfPoints());

        for (int i = 0; i < integrator.getNumberOfPoints(); i++) {
            Assert.assertEquals(weights[i], integrator.getWeight(i).getReal(), 0d);
        }
    }

    @Test
    public void testGetPoints() {
        final double[] points = { 0, 1.2, 3.4 };
        final double[] weights = { 9.8, 7.6, 5.4 };

        final FieldGaussIntegrator<Decimal64> integrator
        = new FieldGaussIntegrator<>(new Pair<>(toDecimal64(points), toDecimal64(weights)));

        Assert.assertEquals(points.length, integrator.getNumberOfPoints());

        for (int i = 0; i < integrator.getNumberOfPoints(); i++) {
            Assert.assertEquals(points[i], integrator.getPoint(i).getReal(), 0d);
        }
    }

    @Test
    public void testIntegrate() {
        final double[] points = { 0, 1, 2, 3, 4, 5 };
        final double[] weights = { 1, 1, 1, 1, 1, 1 };

        final FieldGaussIntegrator<Decimal64> integrator
        = new FieldGaussIntegrator<>(new Pair<>(toDecimal64(points), toDecimal64(weights)));

        final Decimal64 val = new Decimal64(123.456);
        final CalculusFieldUnivariateFunction<Decimal64> c = x -> val;

        final Decimal64 s = integrator.integrate(c);
        Assert.assertEquals(val.multiply(points.length).getReal(), s.getReal(), 0d);
    }

    private Decimal64[] toDecimal64(final double[] a) {
        final Decimal64[] d = new Decimal64[a.length];
        for (int i = 0; i < a.length; ++i) {
            d[i] = new Decimal64(a[i]);
        }
        return d;
    }

}
