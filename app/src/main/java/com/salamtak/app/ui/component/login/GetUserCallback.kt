/*
 * Copyright (c) 2017-present, Facebook, Inc. All rights reserved.
 * <p>
 * You are hereby granted a non-exclusive, worldwide, royalty-free license to use,
 * copy, modify, and distribute this software in source code or binary form for use
 * in connection with the web services and APIs provided by Facebook.
 * <p>
 * As with any software that integrates with the Facebook platform, your use of
 * this software is subject to the Facebook Developer Principles and Policies
 * [http://developers.facebook.com/policy/]. This copyright notice shall be
 * included in all copies or substantial portions of the software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.salamtak.app.ui.component.login

import android.net.Uri
import android.util.Log
import com.facebook.GraphRequest
import com.google.gson.Gson
import com.salamtak.app.data.entities.User
import org.json.JSONException
import org.json.JSONObject

class GetUserCallback(private val mGetUserResponse: IGetUserResponse) {
    interface IGetUserResponse {
        fun onCompleted(user: User?)
    }

    val callback: GraphRequest.Callback
    @Throws(JSONException::class)
    private fun jsonToUser(user: JSONObject): User {
        val picture =
            Uri.parse(user.getJSONObject("picture").getJSONObject("data").getString("url"))
        val name = user.getString("name")
        val id = user.getString("id")
        var email: String? = null
        if (user.has("email")) {
            email = user.getString("email")
        }
        val first_name = user.getString("first_name")
        val last_name = user.getString("last_name")

        // Build permissions display string
        val builder = StringBuilder()
        val perms = user.getJSONObject("permissions").getJSONArray("data")
        builder.append("Permissions:\n")
        for (i in 0 until perms.length()) {
            builder
                .append(perms.getJSONObject(i)["permission"])
                .append(": ")
                .append(perms.getJSONObject(i)["status"])
                .append("\n")
        }
        val permissions = builder.toString()
        return User(picture, name, id, email!!, permissions, first_name, last_name)
    }

    inner class User(
        val picture: Uri,
        val name: String,
        val id: String,
        val email: String,
        val permissions: String,
        private val first_name: String,
        private val last_name: String
    )

    init {
        callback = GraphRequest.Callback { response ->
            var user: User? = null
            try {
                val userObj = response.jsonObject ?: return@Callback
                Log.e("userObj",userObj.toString())
             //   user = Gson().fromJson(userObj, User::class.java)
                user = jsonToUser(userObj)
            } catch (e: JSONException) {
                // Handle exception ...
            }

            // Handled by ProfileActivity
            mGetUserResponse.onCompleted(user)
        }
    }
}