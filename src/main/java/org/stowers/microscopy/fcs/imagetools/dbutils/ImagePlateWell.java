package org.stowers.microscopy.fcs.imagetools.dbutils;

//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;

public class ImagePlateWell {
	
	private int image_id;
	private String plate;
	private String part;
	private int wellnumber;
	private String wellname;
	
	public ImagePlateWell(int image_id, String plate, String part,
							int wellnumber, String wellname) {
		this.image_id = image_id;
		this.plate = plate;
		this.part = part;
		this.wellnumber = wellnumber;
		this.wellname = wellname;
		
	}
	
	public int getImageId() {
		return image_id;
	}
	
	public String getPlate() {
		return plate;
	}
	
	public String getPart() {
		return part;
	}
	
	public int getWellNumber() {
		return wellnumber;
	}
	
	public String getWellName() {
		return wellname;
	}
	
}
