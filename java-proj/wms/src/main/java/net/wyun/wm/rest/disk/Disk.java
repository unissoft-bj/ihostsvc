package net.wyun.wm.rest.disk;

public class Disk {
	
	@Override
	public String toString() {
		return "Disk [root=" + root + ", available=" + available + ", total="
				+ total + "]";
	}
	
	public Disk(String root, String available, String total) {
		super();
		this.root = root;
		this.available = available;
		this.total = total;
	}
	String root;
	String available;
	String total;
	
	public String getRoot() {
		return root;
	}
	public void setRoot(String root) {
		this.root = root;
	}
	public String getAvailable() {
		return available;
	}
	public void setAvailable(String available) {
		this.available = available;
	}
	public String getTotal() {
		return total;
	}
	public void setTotal(String total) {
		this.total = total;
	}
	

}
