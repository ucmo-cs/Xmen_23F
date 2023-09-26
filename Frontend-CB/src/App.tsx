import "./App.css"

import { useForm, SubmitHandler } from "react-hook-form"
import { yupResolver } from "@hookform/resolvers/yup"
import * as yup from "yup";
type formInputs = {
  userName: string, 
  password: string
}

function App() {
	return (
		<>
			<Credential />
		</>
	)
}

const schema = yup.object().shape({
  userName: yup.string().required('Username is required'),
  password: yup.string().required('Password is required'),
});

const Credential = () => {
	const {
		register,
		handleSubmit,
		formState: { errors } } = useForm<formInputs>({
    resolver: yupResolver(schema),
  });
  const onSubmit: SubmitHandler<formInputs> = (data) => console.log(data)
	
  return (
		<div className="min-h-screen flex justify-center items-center bg-emerald-700">
			<div className="bg-slate-100 p-10 rounded shadow max-w-xs">
				<div>
					<img
						className="logoLogin mx-auto rounded-full border-black border bg-white"
						src="2017cbcagreen342with368stackednofdic.png"
						alt="logo"
					/>
				</div>

				<form onSubmit={handleSubmit(onSubmit)} className="p-8 ">
					<div className="mb-4">

						<label
							htmlFor="username"
							className="block text-gray-700 font-bold mb-2">
							Username
						</label>

						<input required
							{...register("userName", { required: true })}
							type="text"
              id="username"
							className={`bg-slate-200 border-b-2 border-slate-600 rounded-sm ${
                errors.userName ? "border-red-500" : ""}`}
            
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
						<input required
							{...register("password", { required: true })}
							type="password"
							className={`bg-slate-200 border-b-2 border-slate-600 rounded-sm ${
                errors.password ? "border-red-500" : ""}`}
              //"bg-slate-200 border-b-2 border-slate-600 rounded-sm"
							placeholder="Enter your password"
						/>
            {errors.password && (
              <p className="text-red-500 text-sm mt-1">
                {errors.password.message}
              </p>
            )}
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

export default App
