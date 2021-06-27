package Users;

import Setup.config;
import TestUtils.TestUtil;
import io.restassured.response.Response;
import org.json.simple.JSONObject;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

public class P1_UsersApi extends TestUtil{
	int userID;
	@Test(priority=1)
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
	@Test(priority=1)
	void post_user_validation_without_first_name()
	{
		JSONObject userBody= new JSONObject();
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
				.statusCode(400)
				.log().body()
				.body("message", equalTo("Validation failed"),
						"subErrors[0].field",equalTo("firstName"),"subErrors[0].message",equalTo("must not be blank"));
	}
	@Test(priority=2)
	void post_user_validation_without_last_name()
	{
		JSONObject userBody= new JSONObject();
		userBody.put("firstName", "FirstName");
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
						"subErrors[0].field",equalTo("lastName"),"subErrors[0].message",equalTo("must not be blank"));
	}
	@Test(priority=3)
	void post_user_validation_invalid_dob_01()
	{
		JSONObject userBody= new JSONObject();
		userBody.put("firstName", "newName");
		userBody.put("lastName", "newLastName");
		userBody.put("email", "workingemail"+System.currentTimeMillis()+"@gmail.com");
		userBody.put("dayOfBirth", "1953-06-2766");

		given()
				.headers("Content-Type","application/json")
				.body(userBody.toJSONString())
				.baseUri(config.BASEURL).

				when()
				.post("/users")
				.then()
				.statusCode(400)

				.log().body()
				.body("message", equalTo("Wrong content-type of the request format. Expected content-type is application/json."),"status", equalTo("BAD_REQUEST"),
						"debugMessage",containsString("JSON parse error: Cannot deserialize value of type `java.time.LocalDate` from String"),"debugMessage",containsString("dayOfBirth") );
	}

	@Test(priority=3)
	void post_user_validation_invalid_dob_02()
	{
		JSONObject userBody= new JSONObject();
		userBody.put("firstName", "newName");
		userBody.put("lastName", "newLastName");
		userBody.put("email", "workingemail"+System.currentTimeMillis()+"@gmail.com");
		userBody.put("dayOfBirth", "06-12-1927");

		given()
				.headers("Content-Type","application/json")
				.body(userBody.toJSONString())
				.baseUri(config.BASEURL).

				when()
				.post("/users")
				.then()
				.statusCode(400)

				.log().body()
				.body("message", equalTo("Wrong content-type of the request format. Expected content-type is application/json."),"status", equalTo("BAD_REQUEST"),
						"debugMessage",containsString("JSON parse error: Cannot deserialize value of type `java.time.LocalDate` from String"),"debugMessage",containsString("dayOfBirth") );
	}
	@Test(priority=4)
	void post_user_validation_invalid_dob_03()
	{
		JSONObject userBody= new JSONObject();
		userBody.put("firstName", "newName");
		userBody.put("lastName", "newLastName");
		userBody.put("email", "workingemail"+System.currentTimeMillis()+"@gmail.com");
		userBody.put("dayOfBirth", "12-30-1927");

		given()
				.headers("Content-Type","application/json")
				.body(userBody.toJSONString())
				.baseUri(config.BASEURL).

				when()
				.post("/users")
				.then()
				.statusCode(400)

				.log().body()
				.body("message", equalTo("Wrong content-type of the request format. Expected content-type is application/json."),"status", equalTo("BAD_REQUEST"),
						"debugMessage",containsString("JSON parse error: Cannot deserialize value of type `java.time.LocalDate` from String"),"debugMessage",containsString("dayOfBirth") );
	}

	@Test(priority=4)
	void put_user_id_validation_01()
	{   userID=99999999;
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
				.statusCode(404);
	}
	@Test(priority=5)
	void put_user_missing_field_validation_01()
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


		userBody= new JSONObject();
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
				.statusCode(400)
				.body("message", equalTo("Validation failed"),
						"subErrors[0].field",equalTo("firstName"),"subErrors[0].message",equalTo("must not be blank"));
	}

	@Test(priority=6)
	void patch_user_dob_field_validation_01()
	{

		JSONObject userBody= new JSONObject();
		userBody.put("dayOfBirth", "07-06-1999");

		given()
				.headers("Content-Type","application/json")
				.pathParam("id", userID)
				.body(userBody.toJSONString())
				.baseUri(config.BASEURL).
				when()
				.patch("/users/{id}")
				.then()
				.statusCode(400)
				.body("message", equalTo("Wrong content-type of the request format. Expected content-type is application/json."),"status", equalTo("BAD_REQUEST"),
						"debugMessage",containsString("dayOfBirth") );
	}

}


