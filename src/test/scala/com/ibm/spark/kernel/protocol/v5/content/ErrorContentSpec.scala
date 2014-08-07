package com.ibm.spark.kernel.protocol.v5.content

import org.scalatest.{Matchers, FunSpec}
import play.api.data.validation.ValidationError
import play.api.libs.json._

class ErrorContentSpec extends FunSpec with Matchers {
  val errorJson: JsValue = Json.parse("""
  {
    "ename": "<STRING>",
    "evalue": "<STRING>",
    "traceback": ["<STRING>"]
  }
  """)

  val error = ErrorContent("<STRING>", "<STRING>", List("<STRING>"))
  
  describe("ErrorContent") {
    describe("implicit conversions") {
      it("should implicitly convert from valid json to a ErrorContent instance") {
        // This is the least safe way to convert as an error is thrown if it fails
        errorJson.as[ErrorContent] should be (error)
      }

      it("should also work with asOpt") {
        // This is safer, but we lose the error information as it returns
        // None if the conversion fails
        val newCompleteRequest = errorJson.asOpt[ErrorContent]

        newCompleteRequest.get should be (error)
      }

      it("should also work with validate") {
        // This is the safest as it collects all error information (not just first error) and reports it
        val CompleteRequestResults = errorJson.validate[ErrorContent]

        CompleteRequestResults.fold(
          (invalid: Seq[(JsPath, Seq[ValidationError])]) => println("Failed!"),
          (valid: ErrorContent) => valid
        ) should be (error)
      }

      it("should implicitly convert from a ErrorContent instance to valid json") {
        Json.toJson(error) should be (errorJson)
      }
    }
  }

}
