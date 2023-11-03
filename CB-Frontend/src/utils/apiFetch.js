import axios from "axios"

/***
 * @param {string} method
 * @param {string} endpoint
 * @param {object} body
 * @param {object} headers
 * @returns {Promise | object | Error}
 * @description
 * This function is a wrapper for axios to make API calls to backend
 * with the correct headers and body.
 * @example
 *  await apiFetch("POST", "/auth/login", { username: "test", password: "test" })
 *  await apiFetch("GET", "/auth/me")
 */

export default async function apiFetch(method, endpoint, body, headers) {
	const axiosParams = { headers: {} }

	const token = localStorage.getItem("token")

	const defaultHeaders = {
		...headers,
	}

	if (token && endpoint !== "/login") {
		// can't send token to login endpoint or it will fail the login :D
		defaultHeaders["Authorization"] = `Bearer ${token}`
	}

	axiosParams.method = method || "GET"
	axiosParams.url = "http://localhost:8080" + endpoint

	const defaultBody = body ? body : null

	axiosParams.data = defaultBody
	axiosParams.headers = defaultHeaders

	return await axios(axiosParams)
		.then(res => {
			return res
		})
		.catch(err => {
			console.log(err.response)
			return err.response
		})
}
