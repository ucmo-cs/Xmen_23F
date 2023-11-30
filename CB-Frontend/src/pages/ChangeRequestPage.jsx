import * as yup from "yup"
import dayjs from "dayjs"
import { useForm } from "react-hook-form"
import { yupResolver } from "@hookform/resolvers/yup"
import React, { useState } from "react"
import NavBar from "../components/NavBar.jsx"
import apiFetch from "../utils/apiFetch.js"
import {
	useMutation,
	useQueryClient,
	QueryClient,
	QueryClientProvider,
} from "@tanstack/react-query"
import toast from "react-hot-toast"
import axios, { HttpStatusCode } from "axios"
import { useNavigate } from "react-router-dom"

const ABCD = () => {
	const queryClient = useQueryClient()
	const nav = useNavigate()

	const { mutateAsync } = useMutation({
		mutationFn: async data => {
			const res = await apiFetch("POST", "/api/v1/change", data, {})
			console.log(res)
		},
		onSuccess: () => {
			queryClient.invalidateQueries({ queryKey: ["changeRequest"] })
			nav("/dashboard")
		},
	})

	return <CreateChangeRequest mutate={mutateAsync} />
}

function CreateChangeRequest({ mutate }) {
	const schema = yup.object().shape({
		authorId: yup.number().required("Author ID is required"),
		changeType: yup
			.string()
			.oneOf(["PLANNED", "UNPLANNED", "EMERGENCY"])
			.required("Change type is required"),
		applicationId: yup.number().required("Application ID is required"),

		timeWindowStart: yup.string().required("Both date and time is required"),
		timeWindowEnd: yup.string().required("Both date and time is required"),

		description: yup.string().required("Description is required"),
		reason: yup.string().required("Reason is required"),
		backoutPlan: yup.string().required("Backout plan is required"),
		timeToRevert: yup.number().required("Time to revert is required"),

		approveOrDeny: yup
			.string()
			.oneOf(["APPROVE", "DENY"])
			.required("Approval status is required"),
		state: yup.string().required("State is required"),
		roles: yup.object().shape({
			name: yup.string().required("Role name is required"),
		}),
		riskLevel: yup
			.string()
			.oneOf(["LOW", "MEDIUM", "HIGH"])
			.required("Risk level is required"),
		implementer: yup.string().required("Implementer is required"),
	})

	const { register, handleSubmit } = useForm({
		// resolver: yupResolver(schema),
	})

	const onSubmit = data => {
		//mutation(data)
		console.log(data)
	}

	return (
		<div className="flex-wrap w-full">
			<NavBar />

			<div className="p-4 rounded-full">
				<div>
					<form onSubmit={handleSubmit(mutate)} className="Form px-4">
						<div className="changeType flex items-center p-2">
							<label className="m-2">Change Type:</label>
							<select
								id="ChangeType"
								required
								{...register("changeType", { required: true })}
								className="bg-slate-200 border border-gray-300 text-gray-900 font-medium rounded-lg p-2"
								placeholder="Select Change Type">
								<option disabled value="">
									Select Change Type
								</option>
								<option value="PLANNED">Planned</option>
								<option value="UNPLANNED">Unplanned</option>
								<option value="EMERGENCY">Emergency</option>
							</select>
						</div>

						<div className="application flex items-center p-2">
							<label className="m-2">ApplicationID:</label>
							<input
								type="number"
								autoComplete="off"
								required
								{...register("applicationId", { required: true })}
								className="flex-box m-2 block p-2 text-gray-900 border-2 border-gray-400 rounded-lg bg-gray-50 outline-none sm:text-md"
							/>
						</div>

						<div className="implementer flex items-center p-2">
							<label className="m-2">Implementer:</label>
							<input
								type="input"
								autoComplete="off"
								required
								{...register("implementer", { required: true })}
								className="flex-box m-2 block p-2 text-gray-900 border-2 border-gray-400 rounded-lg bg-gray-50 outline-none sm:text-md"
							/>
						</div>

						<div className="scheduleDATE/TIME p-2">
							<div className="start flex items-center p-2">
								<label className="m-2">Scheduled Start Date/Time:</label>
								<input
									type="datetime-local"
									required
									{...register("timeWindowStart", { required: true })}
									className="flex-box m-2 block p-2 text-gray-900 border-2 border-gray-400 rounded-lg bg-gray-50 outline-none sm:text-md"
								/>
							</div>
							<div className="end flex items-center p-2">
								<label className="m-2">Scheduled End Date/Time:</label>
								<input
									type="datetime-local"
									required
									{...register("timeWindowEnd", { required: true })}
									className="flex-box m-2 block p-2 text-gray-900 border-2 border-gray-400 rounded-lg bg-gray-50 outline-none sm:text-md"
								/>
							</div>
						</div>

						<div className="description flex-box p-2">
							<h1 className="text-2xl ml-2">Detailed Event Description:</h1>
							<div className="description">
								<label className="m-2">1. What is changing and why?</label>
								<textarea
									className="flex-box resize-none m-2 w-full block p-2 text-gray-900 border-2 border-gray-400 rounded-lg bg-gray-50 outline-none sm:text-md"
									required
									{...register("description", { required: true })}
								/>
							</div>
							<div className="reason">
								<label className="m-2">2. Why is change needed?</label>
								<textarea
									className="flex-box resize-none m-2 w-full block p-2 text-gray-900 border-2 border-gray-400 rounded-lg bg-gray-50 outline-none sm:text-md"
									required
									{...register("reason", { required: true })}
								/>
							</div>
							<div>
								<label className="m-2">Backout Plan:</label>
								<textarea
									className="flex-box resize-none m-2 w-full block p-2 text-gray-900 border-2 border-gray-400 rounded-lg bg-gray-50 outline-none sm:text-md"
									required
									{...register("backoutPlan", { required: true })}
								/>
							</div>
							<div className="flex items-center p-2">
								<label className="m-2">Minutes to execute plan:</label>
								<input
									type="number"
									autoComplete="off"
									className="flex-box m-2 block p-2 text-gray-900 border-2 border-gray-400 rounded-lg bg-gray-50 outline-none sm:text-md"
									required
									{...register("timeToRevert", { required: true })}
								/>
							</div>
							<div className="risk flex p-2">
								<fieldset className="flex items-center p-2">
									<label className="me-4">Risk Assessment:</label>
									<div className="flex items-center me-4">
										<input
											id="riskLow"
											className="peer riskLow"
											type="radio"
											name="risk"
											value="LOW"
											{...register("riskLevel", { required: true })}
										/>
										<label
											htmlFor="riskLow"
											className="peer-checked riskLow font-bold">
											Low
										</label>
									</div>
									<div className="flex items-center me-4">
										<input
											id="riskMed"
											className="peer riskMed"
											type="radio"
											name="risk"
											value="MEDIUM"
											required
											{...register("riskLevel", { required: true })}
										/>
										<label
											htmlFor="riskMed"
											className="peer-checked riskMed font-bold">
											Med
										</label>
									</div>
									<div className="flex items-center me-4">
										<input
											id="riskHigh"
											className="peer riskHigh"
											type="radio"
											name="risk"
											value="High"
											required
											{...register("riskLevel", { required: true })}
										/>
										<label
											htmlFor="riskHigh"
											className="peer-checked riskHigh font-bold">
											High
										</label>
									</div>
								</fieldset>
							</div>
						</div>
						<div className="flex items-center justify-end">
							<div className="Submit">
								<button className="hover:border-black border-2 bg-gray-200 font-bold text-black p-2 rounded-lg m-2">
									Cancel
								</button>
								<button
									type="submit"
									className="hover:border-black border-2 bg-gray-200 font-bold text-black p-2 rounded-lg m-2">
									Submit
								</button>
							</div>
						</div>
					</form>
				</div>
			</div>
		</div>
	)
}
export default ABCD
