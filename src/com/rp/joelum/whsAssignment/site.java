package com.rp.joelum.whsAssignment;

public class site {
	String name;
	String country;
	String url;
	double latitude;
	double longitude;
	
	public site(String name, String country, String url, double latitude,
			double longitude) {
		this.name = name;
		this.country = country;
		this.url = url;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public String toString() {
		return name;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
}
