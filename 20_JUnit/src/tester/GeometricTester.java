/**
 *	How to use JUnit for our own defined classes.
 *
 *	@author 		ITWorks4U
 */
package tester;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import children.*;

class GeometricTester {

	static Cube<Double> cube = null;
	static Pyramid<Double> pyramid = null;
	static Tetrahedron<Double> tetra = null;
	
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		cube = new Cube<>(3.0, 3.0, 3.0);
		pyramid = new Pyramid<>(1.0, 1.0, 0.123456789);
		tetra = new Tetrahedron<>(1.1, 1.1, 1.1);
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
		cube = null;
		pyramid = null;
		tetra = null;
	}

	@Test
	void testCalculateVolume() throws Exception {
//		fail("Not yet implemented");
		
		double volumeCube = (double) cube.calculateVolume();
		double volumePyramid = (double) pyramid.calculateVolume();
		double volumeTetrahedron = (double) tetra.calculateVolume();
		
		Assertions.assertTrue(volumeCube > 15.0);
		Assertions.assertTrue(volumePyramid < 15.0);
		Assertions.assertTrue(volumeTetrahedron < 15.0);
		
		Assertions.assertEquals(volumeCube, 27.1);
	}

	@Test
	void testCalculateArea() {
//		fail("Not yet implemented");
	}
}
