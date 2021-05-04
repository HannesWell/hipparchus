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
package org.hipparchus.util;

import org.hipparchus.RealFieldElementAbstractTest;
import org.junit.Assert;
import org.junit.Test;

public class Decimal64Test extends RealFieldElementAbstractTest<Decimal64> {
    public static final double X = 1.2345;

    public static final Decimal64 PLUS_X = new Decimal64(X);

    public static final Decimal64 MINUS_X = new Decimal64(-X);

    public static final double Y = 6.789;

    public static final Decimal64 PLUS_Y = new Decimal64(Y);

    public static final Decimal64 MINUS_Y = new Decimal64(-Y);

    public static final Decimal64 PLUS_ZERO = new Decimal64(0.0);

    public static final Decimal64 MINUS_ZERO = new Decimal64(-0.0);

    @Override
    protected Decimal64 build(final double x) {
        return new Decimal64(x);
    }

    @Test
    public void testAdd() {
        Decimal64 expected, actual;

        expected = new Decimal64(X + Y);
        actual = PLUS_X.add(PLUS_Y);
        Assert.assertEquals(expected, actual);
        actual = PLUS_Y.add(PLUS_X);
        Assert.assertEquals(expected, actual);

        expected = new Decimal64(X + (-Y));
        actual = PLUS_X.add(MINUS_Y);
        Assert.assertEquals(expected, actual);
        actual = MINUS_Y.add(PLUS_X);
        Assert.assertEquals(expected, actual);

        expected = new Decimal64((-X) + (-Y));
        actual = MINUS_X.add(MINUS_Y);
        Assert.assertEquals(expected, actual);
        actual = MINUS_Y.add(MINUS_X);
        Assert.assertEquals(expected, actual);

        expected = Decimal64.POSITIVE_INFINITY;
        actual = PLUS_X.add(Decimal64.POSITIVE_INFINITY);
        Assert.assertEquals(expected, actual);
        actual = Decimal64.POSITIVE_INFINITY.add(PLUS_X);
        Assert.assertEquals(expected, actual);
        actual = MINUS_X.add(Decimal64.POSITIVE_INFINITY);
        Assert.assertEquals(expected, actual);
        actual = Decimal64.POSITIVE_INFINITY.add(MINUS_X);
        Assert.assertEquals(expected, actual);
        actual = Decimal64.POSITIVE_INFINITY.add(Decimal64.POSITIVE_INFINITY);
        Assert.assertEquals(expected, actual);

        expected = Decimal64.NEGATIVE_INFINITY;
        actual = PLUS_X.add(Decimal64.NEGATIVE_INFINITY);
        Assert.assertEquals(expected, actual);
        actual = Decimal64.NEGATIVE_INFINITY.add(PLUS_X);
        Assert.assertEquals(expected, actual);
        actual = Decimal64.NEGATIVE_INFINITY.add(Decimal64.NEGATIVE_INFINITY);
        Assert.assertEquals(expected, actual);
        actual = MINUS_X.add(Decimal64.NEGATIVE_INFINITY);
        Assert.assertEquals(expected, actual);
        actual = Decimal64.NEGATIVE_INFINITY.add(MINUS_X);
        Assert.assertEquals(expected, actual);

        expected = Decimal64.NAN;
        actual = Decimal64.POSITIVE_INFINITY.add(Decimal64.NEGATIVE_INFINITY);
        Assert.assertEquals(expected, actual);
        actual = Decimal64.NEGATIVE_INFINITY.add(Decimal64.POSITIVE_INFINITY);
        Assert.assertEquals(expected, actual);
        actual = PLUS_X.add(Decimal64.NAN);
        Assert.assertEquals(expected, actual);
        actual = Decimal64.NAN.add(PLUS_X);
        Assert.assertEquals(expected, actual);
        actual = MINUS_X.add(Decimal64.NAN);
        Assert.assertEquals(expected, actual);
        actual = Decimal64.NAN.add(MINUS_X);
        Assert.assertEquals(expected, actual);
        actual = Decimal64.POSITIVE_INFINITY.add(Decimal64.NAN);
        Assert.assertEquals(expected, actual);
        actual = Decimal64.NAN.add(Decimal64.POSITIVE_INFINITY);
        Assert.assertEquals(expected, actual);
        actual = Decimal64.NEGATIVE_INFINITY.add(Decimal64.NAN);
        Assert.assertEquals(expected, actual);
        actual = Decimal64.NAN.add(Decimal64.NEGATIVE_INFINITY);
        Assert.assertEquals(expected, actual);
        actual = Decimal64.NAN.add(Decimal64.NAN);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testSubtract() {
        Decimal64 expected, actual;

        expected = new Decimal64(X - Y);
        actual = PLUS_X.subtract(PLUS_Y);
        Assert.assertEquals(expected, actual);

        expected = new Decimal64(X - (-Y));
        actual = PLUS_X.subtract(MINUS_Y);
        Assert.assertEquals(expected, actual);

        expected = new Decimal64((-X) - Y);
        actual = MINUS_X.subtract(PLUS_Y);
        Assert.assertEquals(expected, actual);

        expected = new Decimal64((-X) - (-Y));
        actual = MINUS_X.subtract(MINUS_Y);
        Assert.assertEquals(expected, actual);

        expected = Decimal64.NEGATIVE_INFINITY;
        actual = PLUS_X.subtract(Decimal64.POSITIVE_INFINITY);
        Assert.assertEquals(expected, actual);
        actual = MINUS_X.subtract(Decimal64.POSITIVE_INFINITY);
        Assert.assertEquals(expected, actual);
        actual = Decimal64.NEGATIVE_INFINITY
                .subtract(Decimal64.POSITIVE_INFINITY);
        Assert.assertEquals(expected, actual);

        expected = Decimal64.POSITIVE_INFINITY;
        actual = PLUS_X.subtract(Decimal64.NEGATIVE_INFINITY);
        Assert.assertEquals(expected, actual);
        actual = MINUS_X.subtract(Decimal64.NEGATIVE_INFINITY);
        Assert.assertEquals(expected, actual);
        actual = Decimal64.POSITIVE_INFINITY
                .subtract(Decimal64.NEGATIVE_INFINITY);
        Assert.assertEquals(expected, actual);

        expected = Decimal64.NAN;
        actual = Decimal64.POSITIVE_INFINITY
                .subtract(Decimal64.POSITIVE_INFINITY);
        Assert.assertEquals(expected, actual);
        actual = Decimal64.NEGATIVE_INFINITY
                .subtract(Decimal64.NEGATIVE_INFINITY);
        Assert.assertEquals(expected, actual);
        actual = PLUS_X.subtract(Decimal64.NAN);
        Assert.assertEquals(expected, actual);
        actual = Decimal64.NAN.subtract(PLUS_X);
        Assert.assertEquals(expected, actual);
        actual = MINUS_X.subtract(Decimal64.NAN);
        Assert.assertEquals(expected, actual);
        actual = Decimal64.NAN.subtract(MINUS_X);
        Assert.assertEquals(expected, actual);
        actual = Decimal64.POSITIVE_INFINITY.subtract(Decimal64.NAN);
        Assert.assertEquals(expected, actual);
        actual = Decimal64.NAN.subtract(Decimal64.POSITIVE_INFINITY);
        Assert.assertEquals(expected, actual);
        actual = Decimal64.NEGATIVE_INFINITY.subtract(Decimal64.NAN);
        Assert.assertEquals(expected, actual);
        actual = Decimal64.NAN.subtract(Decimal64.NEGATIVE_INFINITY);
        Assert.assertEquals(expected, actual);
        actual = Decimal64.NAN.subtract(Decimal64.NAN);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testNegate() {
        Decimal64 expected, actual;

        expected = MINUS_X;
        actual = PLUS_X.negate();
        Assert.assertEquals(expected, actual);

        expected = PLUS_X;
        actual = MINUS_X.negate();
        Assert.assertEquals(expected, actual);

        expected = MINUS_ZERO;
        actual = PLUS_ZERO.negate();
        Assert.assertEquals(expected, actual);

        expected = PLUS_ZERO;
        actual = MINUS_ZERO.negate();
        Assert.assertEquals(expected, actual);

        expected = Decimal64.POSITIVE_INFINITY;
        actual = Decimal64.NEGATIVE_INFINITY.negate();
        Assert.assertEquals(expected, actual);

        expected = Decimal64.NEGATIVE_INFINITY;
        actual = Decimal64.POSITIVE_INFINITY.negate();
        Assert.assertEquals(expected, actual);

        expected = Decimal64.NAN;
        actual = Decimal64.NAN.negate();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testMultiply() {
        Decimal64 expected, actual;

        expected = new Decimal64(X * Y);
        actual = PLUS_X.multiply(PLUS_Y);
        Assert.assertEquals(expected, actual);
        actual = PLUS_Y.multiply(PLUS_X);
        Assert.assertEquals(expected, actual);

        expected = new Decimal64(X * (-Y));
        actual = PLUS_X.multiply(MINUS_Y);
        Assert.assertEquals(expected, actual);
        actual = MINUS_Y.multiply(PLUS_X);
        Assert.assertEquals(expected, actual);

        expected = new Decimal64((-X) * (-Y));
        actual = MINUS_X.multiply(MINUS_Y);
        Assert.assertEquals(expected, actual);
        actual = MINUS_Y.multiply(MINUS_X);
        Assert.assertEquals(expected, actual);

        expected = Decimal64.POSITIVE_INFINITY;
        actual = PLUS_X.multiply(Decimal64.POSITIVE_INFINITY);
        Assert.assertEquals(expected, actual);
        actual = Decimal64.POSITIVE_INFINITY.multiply(PLUS_X);
        Assert.assertEquals(expected, actual);
        actual = MINUS_X.multiply(Decimal64.NEGATIVE_INFINITY);
        Assert.assertEquals(expected, actual);
        actual = Decimal64.NEGATIVE_INFINITY.multiply(MINUS_X);
        Assert.assertEquals(expected, actual);
        actual = Decimal64.POSITIVE_INFINITY
                .multiply(Decimal64.POSITIVE_INFINITY);
        Assert.assertEquals(expected, actual);
        actual = Decimal64.NEGATIVE_INFINITY
                .multiply(Decimal64.NEGATIVE_INFINITY);
        Assert.assertEquals(expected, actual);

        expected = Decimal64.NEGATIVE_INFINITY;
        actual = PLUS_X.multiply(Decimal64.NEGATIVE_INFINITY);
        Assert.assertEquals(expected, actual);
        actual = Decimal64.NEGATIVE_INFINITY.multiply(PLUS_X);
        Assert.assertEquals(expected, actual);
        actual = MINUS_X.multiply(Decimal64.POSITIVE_INFINITY);
        Assert.assertEquals(expected, actual);
        actual = Decimal64.POSITIVE_INFINITY.multiply(MINUS_X);
        Assert.assertEquals(expected, actual);
        actual = Decimal64.POSITIVE_INFINITY
                .multiply(Decimal64.NEGATIVE_INFINITY);
        Assert.assertEquals(expected, actual);
        actual = Decimal64.NEGATIVE_INFINITY
                .multiply(Decimal64.POSITIVE_INFINITY);
        Assert.assertEquals(expected, actual);

        expected = Decimal64.NAN;
        actual = PLUS_X.multiply(Decimal64.NAN);
        Assert.assertEquals(expected, actual);
        actual = Decimal64.NAN.multiply(PLUS_X);
        Assert.assertEquals(expected, actual);
        actual = MINUS_X.multiply(Decimal64.NAN);
        Assert.assertEquals(expected, actual);
        actual = Decimal64.NAN.multiply(MINUS_X);
        Assert.assertEquals(expected, actual);
        actual = Decimal64.POSITIVE_INFINITY.multiply(Decimal64.NAN);
        Assert.assertEquals(expected, actual);
        actual = Decimal64.NAN.multiply(Decimal64.POSITIVE_INFINITY);
        Assert.assertEquals(expected, actual);
        actual = Decimal64.NEGATIVE_INFINITY.multiply(Decimal64.NAN);
        Assert.assertEquals(expected, actual);
        actual = Decimal64.NAN.multiply(Decimal64.NEGATIVE_INFINITY);
        Assert.assertEquals(expected, actual);
        actual = Decimal64.NAN.multiply(Decimal64.NAN);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testDivide() {
        Decimal64 expected, actual;

        expected = new Decimal64(X / Y);
        actual = PLUS_X.divide(PLUS_Y);
        Assert.assertEquals(expected, actual);

        expected = new Decimal64(X / (-Y));
        actual = PLUS_X.divide(MINUS_Y);
        Assert.assertEquals(expected, actual);

        expected = new Decimal64((-X) / Y);
        actual = MINUS_X.divide(PLUS_Y);
        Assert.assertEquals(expected, actual);

        expected = new Decimal64((-X) / (-Y));
        actual = MINUS_X.divide(MINUS_Y);
        Assert.assertEquals(expected, actual);

        expected = PLUS_ZERO;
        actual = PLUS_X.divide(Decimal64.POSITIVE_INFINITY);
        Assert.assertEquals(expected, actual);
        actual = MINUS_X.divide(Decimal64.NEGATIVE_INFINITY);
        Assert.assertEquals(expected, actual);

        expected = MINUS_ZERO;
        actual = MINUS_X.divide(Decimal64.POSITIVE_INFINITY);
        Assert.assertEquals(expected, actual);
        actual = PLUS_X.divide(Decimal64.NEGATIVE_INFINITY);
        Assert.assertEquals(expected, actual);

        expected = Decimal64.POSITIVE_INFINITY;
        actual = Decimal64.POSITIVE_INFINITY.divide(PLUS_X);
        Assert.assertEquals(expected, actual);
        actual = Decimal64.NEGATIVE_INFINITY.divide(MINUS_X);
        Assert.assertEquals(expected, actual);
        actual = PLUS_X.divide(PLUS_ZERO);
        Assert.assertEquals(expected, actual);
        actual = MINUS_X.divide(MINUS_ZERO);
        Assert.assertEquals(expected, actual);

        expected = Decimal64.NEGATIVE_INFINITY;
        actual = Decimal64.POSITIVE_INFINITY.divide(MINUS_X);
        Assert.assertEquals(expected, actual);
        actual = Decimal64.NEGATIVE_INFINITY.divide(PLUS_X);
        Assert.assertEquals(expected, actual);
        actual = PLUS_X.divide(MINUS_ZERO);
        Assert.assertEquals(expected, actual);
        actual = MINUS_X.divide(PLUS_ZERO);
        Assert.assertEquals(expected, actual);

        expected = Decimal64.NAN;
        actual = Decimal64.POSITIVE_INFINITY
                .divide(Decimal64.POSITIVE_INFINITY);
        Assert.assertEquals(expected, actual);
        actual = Decimal64.POSITIVE_INFINITY
                .divide(Decimal64.NEGATIVE_INFINITY);
        Assert.assertEquals(expected, actual);
        actual = Decimal64.NEGATIVE_INFINITY
                .divide(Decimal64.POSITIVE_INFINITY);
        Assert.assertEquals(expected, actual);
        actual = Decimal64.NEGATIVE_INFINITY
                .divide(Decimal64.NEGATIVE_INFINITY);
        Assert.assertEquals(expected, actual);
        actual = PLUS_X.divide(Decimal64.NAN);
        Assert.assertEquals(expected, actual);
        actual = Decimal64.NAN.divide(PLUS_X);
        Assert.assertEquals(expected, actual);
        actual = MINUS_X.divide(Decimal64.NAN);
        Assert.assertEquals(expected, actual);
        actual = Decimal64.NAN.divide(MINUS_X);
        Assert.assertEquals(expected, actual);
        actual = Decimal64.POSITIVE_INFINITY.divide(Decimal64.NAN);
        Assert.assertEquals(expected, actual);
        actual = Decimal64.NAN.divide(Decimal64.POSITIVE_INFINITY);
        Assert.assertEquals(expected, actual);
        actual = Decimal64.NEGATIVE_INFINITY.divide(Decimal64.NAN);
        Assert.assertEquals(expected, actual);
        actual = Decimal64.NAN.divide(Decimal64.NEGATIVE_INFINITY);
        Assert.assertEquals(expected, actual);
        actual = Decimal64.NAN.divide(Decimal64.NAN);
        Assert.assertEquals(expected, actual);
        actual = PLUS_ZERO.divide(PLUS_ZERO);
        Assert.assertEquals(expected, actual);
        actual = PLUS_ZERO.divide(MINUS_ZERO);
        Assert.assertEquals(expected, actual);
        actual = MINUS_ZERO.divide(PLUS_ZERO);
        Assert.assertEquals(expected, actual);
        actual = MINUS_ZERO.divide(MINUS_ZERO);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testReciprocal() {
        Decimal64 expected, actual;

        expected = new Decimal64(1.0 / X);
        actual = PLUS_X.reciprocal();
        Assert.assertEquals(expected, actual);

        expected = new Decimal64(1.0 / (-X));
        actual = MINUS_X.reciprocal();
        Assert.assertEquals(expected, actual);

        expected = PLUS_ZERO;
        actual = Decimal64.POSITIVE_INFINITY.reciprocal();
        Assert.assertEquals(expected, actual);

        expected = MINUS_ZERO;
        actual = Decimal64.NEGATIVE_INFINITY.reciprocal();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIsInfinite() {
        Assert.assertFalse(MINUS_X.isInfinite());
        Assert.assertFalse(PLUS_X.isInfinite());
        Assert.assertFalse(MINUS_Y.isInfinite());
        Assert.assertFalse(PLUS_Y.isInfinite());
        Assert.assertFalse(Decimal64.NAN.isInfinite());

        Assert.assertTrue(Decimal64.NEGATIVE_INFINITY.isInfinite());
        Assert.assertTrue(Decimal64.POSITIVE_INFINITY.isInfinite());
    }

    @Test
    public void testIsNaN() {
        Assert.assertFalse(MINUS_X.isNaN());
        Assert.assertFalse(PLUS_X.isNaN());
        Assert.assertFalse(MINUS_Y.isNaN());
        Assert.assertFalse(PLUS_Y.isNaN());
        Assert.assertFalse(Decimal64.NEGATIVE_INFINITY.isNaN());
        Assert.assertFalse(Decimal64.POSITIVE_INFINITY.isNaN());

        Assert.assertTrue(Decimal64.NAN.isNaN());
    }

    @Test
    public void testToDegreesDefinition() {
        double epsilon = 3.0e-16;
        for (int maxOrder = 0; maxOrder < 6; ++maxOrder) {
            for (double x = 0.1; x < 1.2; x += 0.001) {
                Decimal64 value = new Decimal64(x);
                Assert.assertEquals(FastMath.toDegrees(x), value.toDegrees().getReal(), epsilon);
            }
        }
    }

    @Test
    public void testToRadiansDefinition() {
        double epsilon = 3.0e-16;
        for (int maxOrder = 0; maxOrder < 6; ++maxOrder) {
            for (double x = 0.1; x < 1.2; x += 0.001) {
                Decimal64 value = new Decimal64(x);
                Assert.assertEquals(FastMath.toRadians(x), value.toRadians().getReal(), epsilon);
            }
        }
    }

    @Test
    public void testDegRad() {
        for (double x = 0.1; x < 1.2; x += 0.001) {
            Decimal64 value = new Decimal64(x);
            Decimal64 rebuilt = value.toDegrees().toRadians();
            Decimal64 zero = rebuilt.subtract(value);
            Assert.assertEquals(zero.getReal(), 0, 3.0e-16);
        }
    }

    @Test
    public void testRootNegative() {
        final Decimal64 neg64      = new Decimal64(-64);
        final Decimal64 root3Neg64 = neg64.rootN(3);
        final Decimal64 root2Neg64 = neg64.rootN(2);
        Assert.assertEquals(-4.0, root3Neg64.getReal(), 1.0e-15);
        Assert.assertTrue(root2Neg64.isNaN());
    }

    @Test
    public void testSignedZeroEquality() {
        Assert.assertFalse(new Decimal64(1.0).isZero());
        Assert.assertTrue(new Decimal64(-0.0).isZero());
        Assert.assertTrue(new Decimal64(+0.0).isZero());
        Assert.assertFalse(new Decimal64(+0.0).equals(new Decimal64(-0.0)));
    }

    @Test
    public void testValues() {
        Assert.assertEquals(1,    new Decimal64(1.2).byteValue());
        Assert.assertEquals(1,    new Decimal64(1.2).shortValue());
        Assert.assertEquals(1,    new Decimal64(1.2).intValue());
        Assert.assertEquals(1l,   new Decimal64(1.2).longValue());
        Assert.assertEquals(1.2f, new Decimal64(1.2).floatValue(),  0.00001f);
        Assert.assertEquals(1.2 , new Decimal64(1.2).doubleValue(), 1.0e-15);
    }

    @SuppressWarnings("unlikely-arg-type")
    @Test
    public void testEquals() {
        Assert.assertTrue(new Decimal64(1.25).equals(new Decimal64(1.0).add(new Decimal64(0.25))));
        Assert.assertFalse(new Decimal64(1.25).equals(new Decimal64(1.0).add(new Decimal64(1.25))));
        Assert.assertFalse(new Decimal64(1.25).equals("1.25"));
    }

}
