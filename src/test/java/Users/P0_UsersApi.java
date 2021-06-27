package Users;

import Setup.config;
import io.restassured.response.Response;
import org.json.simple.JSONObject;
import org.testng.annotations.Test;
import Utils.Util;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import TestUtils.TestUtil;

public class P0_UsersApi extends TestUtil{
	int userID;

	@Test(priority=1)
	void post_users_01()
	{

		JSONObject userBody= new JSONObject();
		userBody.put("firstName", "test");
		userBody.put("lastName", "user");
		userBody.put("email", "workingemail"+System.currentTimeMillis()+"@gmail.com");
		userBody.put("dayOfBirth", "1953-06-27");

	Response res =
		given()
	     .headers("Content-Type","application/json")
	     .body(userBody.toJSONString())
		.baseUri(config.BASEURL).

	  when()
	     .post("/users")
	.then()
	     .statusCode(201)

		 .log().body()
		.body("firstName", equalTo(userBody.get("firstName")),
		"lastName",equalTo(userBody.get("lastName")),"dayOfBirth",equalTo(userBody.get("dayOfBirth")))
			.extract().response();
	userID=res.path("id");


	}

	@Test(priority=2)
	void put_users_01()
	{
		JSONObject userBody= new JSONObject();
		userBody.put("firstName", "newName");
		userBody.put("lastName", "newLastName");
		userBody.put("email", "workingemailnew"+System.currentTimeMillis()+"@gmail.com");
		userBody.put("dayOfBirth", "1959-06-27");

		given()
		.headers("Content-Type","application/json")
		.pathParam("id", userID)
	     .body(userBody.toJSONString())
				.baseUri(config.BASEURL).
	  when()
	     .put("/users/{id}")
	.then()
	     .statusCode(200)

		 .log().all()
				.body("firstName", equalTo(userBody.get("firstName")),
						"lastName",equalTo(userBody.get("lastName")),"dayOfBirth",equalTo(userBody.get("dayOfBirth")))
				.extract().response();

	}

	@Test(priority=3)
	void put_user_mand_dob_field_validation_01()
	{
		JSONObject userBody= new JSONObject();
		userBody.put("firstName", "test");
		userBody.put("lastName", "user");
		userBody.put("email", "workingemail"+System.currentTimeMillis()+"@gmail.com");


		given()
				.headers("Content-Type","application/json")
				.body(userBody.toJSONString())
				.baseUri(config.BASEURL)
				.pathParam("id", userID).
				when()
				.put("/users/{id}")
				.then()
				.statusCode(400)

				.log().body()
				.body("message", equalTo("Validation failed"),
						"subErrors[0].field",equalTo("dayOfBirth"),"subErrors[0].message",equalTo("must not be null"));

	}


	@Test(priority=4)
	void patch_user_01()
	{ 	System.out.println(userID);
		JSONObject userBody= new JSONObject();
		userBody.put("firstName", "updatedName");

		given()
				.headers("Content-Type","application/json")
				.pathParam("id", userID)
				.body(userBody.toJSONString())
				.baseUri(config.BASEURL).
				when()
				.patch("/users/{id}")
				.then()
				.statusCode(200)
				.log().all()
				.body("firstName", equalTo(userBody.get("firstName")))
				.extract().response();

	}
	@Test(priority=5)
	void get_user_by_id_01()
	{
		given()
				.headers("Content-Type","application/json")
				.pathParam("id", userID)
				.baseUri(config.BASEURL)
				.when()
				.get("/users/{id}")
				.then()
				.statusCode(200)
				.log().body()
				.body("firstName", equalTo("updatedName"));
	}
	@Test(priority=6)
	void post_user_firstName_validation_01()
	{

		JSONObject userBody= new JSONObject();
		userBody.put("firstName", "q");
		userBody.put("lastName", "user");
		userBody.put("email", "workingemail"+System.currentTimeMillis()+"@gmail.com");
		userBody.put("dayOfBirth", "1953-06-27");

				given()
						.headers("Content-Type","application/json")
						.body(userBody.toJSONString())
						.baseUri(config.BASEURL).

						when()
						.post("/users")
						.then()
						.statusCode(400)

						.log().body()
						.body("message", equalTo("Validation failed"),
								"subErrors[0].field",equalTo("firstName"),"subErrors[0].message",equalTo("size must be between 2 and 30"));
		userBody= new JSONObject();
		userBody.put("firstName", "vbfdsfgddgdsgdgdgdgdgdgdgdgdded");
		userBody.put("lastName", "user");
		userBody.put("email", "workingemail"+System.currentTimeMillis()+"@gmail.com");
		userBody.put("dayOfBirth", "1953-06-27");

		given()
				.headers("Content-Type","application/json")
				.body(userBody.toJSONString())
				.baseUri(config.BASEURL).

				when()
				.post("/users")
				.then()
				.statusCode(400)

				.log().body()
				.body("message", equalTo("Validation failed"),
						"subErrors[0].field",equalTo("firstName"),"subErrors[0].message",equalTo("size must be between 2 and 30"));

	}


	@Test(priority=7)
	void post_user_lastName_validation_01()
	{

		JSONObject userBody= new JSONObject();
		userBody.put("firstName", "qa");
		userBody.put("lastName", "u");
		userBody.put("email", "workingemail"+System.currentTimeMillis()+"@gmail.com");
		userBody.put("dayOfBirth", "1953-06-27");

		given()
				.headers("Content-Type","application/json")
				.body(userBody.toJSONString())
				.baseUri(config.BASEURL).

				when()
				.post("/users")
				.then()
				.statusCode(400)

				.log().body()
				.body("message", equalTo("Validation failed"),
						"subErrors[0].field",equalTo("lastName"),"subErrors[0].message",equalTo("size must be between 2 and 15"));
		userBody= new JSONObject();
		userBody.put("firstName", "qa");
		userBody.put("lastName", "userddddcdvdcfdvcdfgcdsa");
		userBody.put("email", "workingemail"+System.currentTimeMillis()+"@gmail.com");
		userBody.put("dayOfBirth", "1953-06-27");

		given()
				.headers("Content-Type","application/json")
				.body(userBody.toJSONString())
				.baseUri(config.BASEURL).

				when()
				.post("/users")
				.then()
				.statusCode(400)

				.log().body()
				.body("message", equalTo("Validation failed"),
						"subErrors[0].field",equalTo("lastName"),"subErrors[0].message",equalTo("size must be between 2 and 15"));

	}
	@Test(priority=8)
	void post_user_dob_validation_01()
	{

		JSONObject userBody= new JSONObject();
		userBody.put("firstName", "test");
		userBody.put("lastName", "user");
		userBody.put("email", "workingemail"+System.currentTimeMillis()+"@gmail.com");


				given()
						.headers("Content-Type","application/json")
						.body(userBody.toJSONString())
						.baseUri(config.BASEURL).

						when()
						.post("/users")
						.then()
						.statusCode(400)

						.log().body()
						.body("message", equalTo("Validation failed"),
								"subErrors[0].field",equalTo("dayOfBirth"),"subErrors[0].message",equalTo("must not be null"));


	}
	@Test(priority=9)
	void post_user_email_unique_validation_01()
	{
       String email="workingemail"+System.currentTimeMillis()+"@gmail.com";
		JSONObject userBody= new JSONObject();
		userBody.put("firstName", "test");
		userBody.put("lastName", "user");
		userBody.put("email", email);
		userBody.put("dayOfBirth", "1953-06-27");

		given()
				.headers("Content-Type","application/json")
				.body(userBody.toJSONString())
				.baseUri(config.BASEURL).

				when()
				.post("/users")
				.then()
				.statusCode(201)

				.log().body();


		userBody= new JSONObject();
		userBody.put("firstName", "newName");
		userBody.put("lastName", "newLastName");
		userBody.put("email", email);
		userBody.put("dayOfBirth", "1953-06-27");

		given()
				.headers("Content-Type","application/json")
				.body(userBody.toJSONString())
				.baseUri(config.BASEURL).

				when()
				.post("/users")
				.then()
				.statusCode(409)

				.log().body()
				.body("message", equalTo("Database error"),"status", equalTo("CONFLICT"),
						"debugMessage",containsString("Unique index or primary key violation"),"debugMessage",containsString("PUBLIC.USER(EMAIL)") );






	}

	@Test(priority=10)
	void post_user_email_validation_02()
	{

		JSONObject userBody= new JSONObject();
		userBody.put("firstName", "test");
		userBody.put("lastName", "user");
		userBody.put("email", "xyz.com");
		userBody.put("dayOfBirth", "1953-06-27");

		given()
				.headers("Content-Type","application/json")
				.body(userBody.toJSONString())
				.baseUri(config.BASEURL).

				when()
				.post("/users")
				.then()
				.statusCode(400)

				.log().body()
				.body("message", equalTo("Validation failed"),
						"subErrors[0].field",equalTo("email"),"subErrors[0].message",equalTo("must be a well-formed email address"));



	}

	@Test(priority=11)
	void post_user_with_id_validation_01()
	{

		JSONObject userBody= new JSONObject();
		userBody.put("id", 1);
		userBody.put("firstName", "newName");
		userBody.put("lastName", "newLastName");
		userBody.put("email", "workingemail"+System.currentTimeMillis()+"@gmail.com");
		userBody.put("dayOfBirth", "1953-06-27");

		given()
				.headers("Content-Type","application/json")
				.body(userBody.toJSONString())
				.baseUri(config.BASEURL).

				when()
				.post("/users")
				.then()
				.statusCode(201)

				.log().body();





	}

}


