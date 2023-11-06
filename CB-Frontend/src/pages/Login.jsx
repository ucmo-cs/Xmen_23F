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

	const handleCreateUser = () => {
		window.location.href = "http://localhost:5173/createuser";
	}

	return (
		<div className="min-h-screen flex justify-center items-center bg-emerald-700">
			<div className="bg-slate-100 p-6 rounded shadow max-w-xs">
				<div>
					<img
						className="logoLogin m-auto rounded-full border-black border bg-white w-auto h-auto"
						src="2017cbcagreen342with368stackednofdic.png"
						alt="logo"
					/>
				</div>

				<form onSubmit={handleSubmit(onSubmit)} className="p-6">
					<Toaster position="bottom-center" />
					<div className="m-4">
						<label
							htmlFor="username"
							className="block text-gray-700 font-bold mb-2 text-lg">
							Username
						</label>
						<input
							required
							{...register("userName", { required: true })}
							type="text"
							id="username"
							className="block w-full p-2 text-gray-900 border-2 border-gray-400 rounded-lg bg-gray-50 outline-none sm:text-md"
							placeholder="Enter your username"
						/>
					</div>
					<div className="m-4">
						<label
							htmlFor="password"
							className="block text-gray-700 font-bold mb-2 text-lg">
							Password
						</label>
						<input
							required
							{...register("password", { required: true })}
							type="password"
							className="block w-full p-2 text-gray-900 border-2 border-gray-400 rounded-lg bg-gray-50 outline-none sm:text-md"
							placeholder="Enter your password"
						/>
					</div>
					<div className="flex justify-center items-center flex-col">
						<button
							type="submit"
							className="hover:border-black border-2 bg-emerald-700 font-bold text-white p-2 rounded-lg m-2 w-full"
						>
							Login
						</button>
						<button
							className="hover:border-black border-2 bg-emerald-700 font-bold text-white p-2 rounded-lg m-2 w-full"
							onClick={handleCreateUser}
						>
							Create User
						</button>
					</div>
				</form>
			</div>
		</div>
	)
}
