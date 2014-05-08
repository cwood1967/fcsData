package org.stowers.microscopy.segmentation;

import ij.ImagePlus;
import junit.framework.TestCase;

public class YeastSRMTest extends TestCase {
    
    public YeastSRMTest(String name) {
        super(name);
    }

    public void testSrmAverages() throws Exception {

        int width= 16;
        int height = 16;

        byte[] imgArray = new byte[256];
        for (int j = 0;j < 16; j++) {
            for (int i = 0; i < 16; i++) {
                int index = j*width + i;

                if ((i < 4) & (j < 4)) {
                    imgArray[index] = (byte)50;
                }
                else if ((i > 11) & (j > 11)) {
                    imgArray[index] = (byte)200;
                }
                else {
                    imgArray[index] = (byte)134;
                }
            }
        }
        YeastSrm srm = new YeastSrm(16,16, imgArray);
        srm.setQval(10.f);
        float[] resArray = srm.ijSRMavg();
        for (int i = 0; i < 256; i++) {
            System.out.println(resArray[i]); 
        }

        assertNotNull(resArray);
    }

    public void testSrmRegions() throws Exception {

        int width= 16;
        int height = 16;

        byte[] imgArray = new byte[256];
        for (int j = 0;j < 16; j++) {
            for (int i = 0; i < 16; i++) {
                int index = j*width + i;

                if ((i < 4) & (j < 4)) {
                    imgArray[index] = (byte)50;
                }
                else if ((i > 11) & (j > 11)) {
                    imgArray[index] = (byte)200;
                }
                else {
                    imgArray[index] = (byte)134;
                }
            }
        }
        YeastSrm srm = new YeastSrm(16,16, imgArray);
        srm.setQval(10.f);
        int[] resArray = srm.ijSRMregions();
        for (int i = 0; i < 256; i++) {
            System.out.println(resArray[i]); 
        }

        assertNotNull(resArray);
    }
}
