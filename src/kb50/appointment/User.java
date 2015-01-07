package kb50.appointment;


public class User {

	private int id;
	private String name;
	private int phone;
	private String email;
	private String pwd;
	private String imageurl;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPhone() {
		return phone;
	}

	public void setPhone(int phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getImageurl() {
		return imageurl;
	}

	public void setImageurl(String imageurl) {
		this.imageurl = imageurl;
	}

}
