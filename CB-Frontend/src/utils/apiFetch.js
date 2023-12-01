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

axios.defaults.baseURL = "http://153.91.194.28:8080/"

export default async function apiFetch(
	method,
	endpoint,
	body = null,
	headers = {}
) {
	const token = localStorage.getItem("token")

	if (endpoint !== "/login") {
		headers["Authorization"] = `Bearer ${token}`
	}

	const response = await axios({
		method: method || "GET", // default to GET if no method is passed
		url: endpoint,
		data: body,
		headers: headers,
	})
		.then(res => {
			console.log(res)
			return res
		})
		.catch(err => {
			console.log(err)
			return err.response
		})
	return response
}
