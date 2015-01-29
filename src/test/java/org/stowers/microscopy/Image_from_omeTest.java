package org.stowers.microscopy;

import ij.ImagePlus;
import junit.framework.TestCase;

public class Image_from_omeTest extends TestCase {
    
    public Image_from_omeTest(String name) {
        super(name);
    }

    public void testGetImage() throws Exception {
        int image_id = 43245;
        Image_from_ome ifo = new Image_from_ome();
        ifo.setImageId(image_id);
        ImagePlus image = ifo.getImage();
        System.out.println(image.getHeight());
        assertNotNull(image);
    }
    
}
