import axios from "axios"
import { useState } from "react"
import * as yup from "yup"
import { Link } from "react-router-dom"
import { useForm } from "react-hook-form"
import toast, { Toaster } from "react-hot-toast"
import { useNavigate } from "react-router-dom"
import { yupResolver } from "@hookform/resolvers/yup"
import { useQuery } from "react-query"
import apiFetch from "../utils/apiFetch"

const schema = yup.object().shape({
	username: yup.string().required("Username is required"),
	password: yup.string().required("Password is required"),
	roles: yup.object().shape({
		name: yup.string().required("Role is required"),
	}),
})

export default function Register() {
	const {
		register,
		handleSubmit,
		formState: { isValid },
	} = useForm({
		resolver: yupResolver(schema),
	})

	const navigate = useNavigate()

	const onSubmit = data => {
		console.log(data)
		const { userName, password, roles } = data

		const registerUser = async () => {
			try {
				const response = await apiFetch("POST", "/api/v1/user", data)

				if (response.status === 200) {
					navigate("/")
				} else if (data) {
					toast.error("Username already exist")
				} else {
					console.log("Registration failed")
				}
			} catch (error) {
				console.error("Error during registration:", error)
			}
		}
		registerUser()
	}

	return (
		<div className="min-h-screen flex justify-center items-center bg-emerald-700">
			<div className="bg-slate-100 p-6 rounded shadow max-w-xs">
				<h1 className="text-2xl block text-gray-700 font-bold mb-2">
					Register
				</h1>
				<form onSubmit={handleSubmit(onSubmit)} className="p-6">
					<Toaster position="bottom-center" />
					<div>
						<div className="m-4">
							<label
								htmlFor="username"
								className="block text-gray-700 font-bold mb-2 text-lg">
								Username:
							</label>
							<input
								required
								{...register("username", { required: true })}
								type="text"
								id="username"
								autoComplete="off"
								className="block w-full p-2 text-gray-900 border-2 border-gray-400 rounded-lg bg-gray-50 outline-none sm:text-md"
								placeholder="Enter your username"
							/>
						</div>
						<div className="m-4">
							<label
								htmlFor="password"
								className="block text-gray-700 font-bold mb-2 text-lg">
								Password:
							</label>
							<input
								required
								{...register("password", { required: true })}
								type="password"
								autoComplete="off"
								className="block w-full p-2 text-gray-900 border-2 border-gray-400 rounded-lg bg-gray-50 outline-none sm:text-md"
								placeholder="Enter your password"
							/>
						</div>
						<div className="relative inline-block text-left mb-10 ml-4 mt-2">
							<label
								htmlFor="roles"
								className="block text-black font-bold text-lg mb-2 ">
								Role:
							</label>
							<select
								id="roleselection"
								required
								{...register("roles.name", { required: true })} // Updated here to match the schema
								className="bg-slate-200 border border-gray-300 text-gray-900 font-medium rounded-lg p-2.5">
								<option disabled value="">
									Select Role
								</option>
								<option value="USER">User</option>
							</select>
						</div>
					</div>
					<div className="flex justify-center items-center flex-col">
						<button
							type="submit"
							className={`hover:border-black border-2 bg-emerald-700 font-bold text-white p-2 rounded-lg m-2 ${
								!isValid ? "opacity-50 cursor-not-allowed" : ""
							}`}
							disabled={!isValid}>
							Sign Up
						</button>
						<label
							htmlFor="Registered"
							className="block text-gray-700 font-bold mb-2 text-lg">
							Already registered?
						</label>
						<Link
							className="inline-block text-blue-500 font-semibold mb-2 text-lg underline p-4"
							to="/">
							Sign In
						</Link>
					</div>
				</form>
			</div>
		</div>
	)
}
