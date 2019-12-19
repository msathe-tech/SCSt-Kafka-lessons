package com.example.demo;

public class RegionWithClicks {
	private String region;
	private long clicks;

	public RegionWithClicks(String region, long clicks) {
		this.region = region;
		this.clicks = clicks;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public long getClicks() {
		return clicks;
	}

	public void setClicks(long clicks) {
		this.clicks = clicks;
	}
}
