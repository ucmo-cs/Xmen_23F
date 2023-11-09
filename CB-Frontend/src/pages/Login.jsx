import { yupResolver } from "@hookform/resolvers/yup"
import axios from "axios"
import { useState } from "react"
import * as yup from "yup"
import { useForm } from "react-hook-form"
import toast, { Toaster } from "react-hot-toast"
import { useNavigate } from "react-router-dom"
import apiFetch from "../utils/apiFetch"

const schema = yup.object().shape({
	userName: yup.string().required("Username is required"),
	password: yup.string().required("Password is required"),
})

export default function () {
	const { register, handleSubmit } = useForm()

	const [token, setToken] = useState("")

	const navigate = useNavigate()

	const onSubmit = data => {
		const { userName, password } = data

		const login = async () => {
			const token = await apiFetch("POST", "/login", {
				username: userName,
				password: password,
			})
			console.log(token)

			if (token && token.status === 200) {
				localStorage.setItem("token", token.data)
				navigate("/dashboard")
			} else if (token.status === 401) {
				toast.error("Invalid username or password")
			} else {
				toast.error("Something went wrong")
			}
		}

		login()
	}

	return (
		<div className="min-h-screen flex justify-center items-center bg-emerald-700">
			<div className="bg-slate-100 p-10 rounded shadow max-w-xs">
				<div>
					<img
						className="logoLogin mx-auto rounded-full border-black border bg-white w-24 h-24"
						src="2017cbcagreen342with368stackednofdic.png"
						alt="logo"
					/>
				</div>

				<form onSubmit={handleSubmit(onSubmit)} className="p-8 ">
					<Toaster position="bottom-center" />
					<div className="mb-4">
						<label
							htmlFor="username"
							className="block text-gray-700 font-bold mb-2">
							Username
						</label>

						<input
							required
							{...register("userName", { required: true })}
							type="text"
							id="username"
							className={`bg-slate-200 border-b-2 border-slate-600 rounded-sm `}
							//"bg-slate-200 border-b-2 border-slate-600 rounded-sm"
							placeholder="Enter your username"
						/>
					</div>
					<div className="mb-6">
						<label
							htmlFor="password"
							className="block text-gray-700 font-bold mb-2">
							Password
						</label>
						<input
							required
							{...register("password", { required: true })}
							placeholder="Enter your password"
							type="password"
							className={`bg-slate-200 border-b-2 border-slate-600 rounded-sm `}
							//"bg-slate-200 border-b-2 border-slate-600 rounded-sm"
						/>
					</div>
					<div className="flex justify-center items-center">
						<button
							className="hover:shadow-lg bg-emerald-700 font-bold text-white p-2 rounded-lg"
							value="login">
							Login
						</button>
					</div>
				</form>
			</div>
		</div>
	)
}
