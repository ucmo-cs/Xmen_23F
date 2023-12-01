import * as yup from "yup"
import dayjs from "dayjs"
import { useForm } from "react-hook-form"
import { yupResolver } from "@hookform/resolvers/yup"
import React, { useEffect, useState } from "react"
import NavBar from "../components/NavBar.jsx"
import apiFetch from "../utils/apiFetch.js"
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query"
import toast from "react-hot-toast"
import { Link, useNavigate, useParams } from "react-router-dom"
import LoadSpinner from "../components/LoadSpinner.jsx"

const GrabChangeRequest = () => {
	const nav = useNavigate()
	const queryClient = useQueryClient()
	let { id } = useParams()

	const [editChangeRequest, setEditChangeRequest] = useState(false)
	const { isLoading, isError, data } = useQuery({
		queryKey: ["SingleChangeRequest"],
		queryFn: async () => {
			const res = await apiFetch("GET", `/api/v1/change/${id}`, {}, {})
			console.log("API response:", res);
 			return res.data
		},
	})

	const { mutateAsync } = useMutation({
		mutationFn: async (data) => {
			console.log('Data to be sent:', data);
			const res = await apiFetch("PUT", `/api/v1/change/${id}`, data, {
				headers: {
					'Content-Type': 'application/json'
				},
			});
			console.log('Response from server:', res);
		},
		onSuccess: () => {
			queryClient.invalidateQueries({ queryKey: ["changeRequest"] });
			nav("/dashboard");
		},
	});

	const { mutateAsync: ApproveChangeRequest } = useMutation({
		mutationFn: async data => {
			const res = await apiFetch("PATCH", `/api/v1/change/${id}/approved`, {})
			console.log(res)
		},
		onSuccess: () => {
			queryClient.invalidateQueries({ queryKey: ["changeRequest"] })
			nav("/dashboard")
		},
		onError: () => {
			toast.error("Something went wrong")
		},
	})

	const { mutateAsync: denyChangeRequest } = useMutation({
		mutationFn: async data => {
			const res = await apiFetch("PATCH", `/api/v1/change/${id}/denied`, {})
			console.log(res)
		},
		onSuccess: () => {
			queryClient.invalidateQueries({ queryKey: ["changeRequest"] })
			nav("/dashboard")
		},
		onError: () => {
			toast.error("Something went wrong")
		},
	})

	if (isLoading) {
		return <LoadSpinner />
	}

	return (
		<CreateChangeRequest
			approveChangeRequest={ApproveChangeRequest}
			saveChangeRequest={mutateAsync}
			editChangeRequest={editChangeRequest}
			ChangeRequest={data}
			denyChangeRequest={denyChangeRequest}
		/>
	)
}

function CreateChangeRequest({
	approveChangeRequest,
	editChangeRequest,
	saveChangeRequest,
	ChangeRequest,
	denyChangeRequest,
}) {
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

	const { register, handleSubmit, setValue } = useForm({
		// resolver: yupResolver(schema),
	})

	useEffect(() => {
		if (ChangeRequest) {
			setValue("authorId", ChangeRequest.authorId || "");
			setValue("changeType", ChangeRequest.changeType || "");
			setValue("applicationId", ChangeRequest.applicationId || "");
			setValue("timeWindowStart", ChangeRequest.timeWindowStart
				? dayjs(ChangeRequest.timeWindowStart).format("YYYY-MM-DDTHH:mm")
				: ""
			);
			setValue("timeWindowEnd", ChangeRequest.timeWindowEnd
				? dayjs(ChangeRequest.timeWindowEnd).format("YYYY-MM-DDTHH:mm")
				: ""
			);
			setValue("description", ChangeRequest.description || "");
			setValue("reason", ChangeRequest.reason || "");
			setValue("backoutPlan", ChangeRequest.backoutPlan || "");
			setValue("timeToRevert", ChangeRequest.timeToRevert || "");
			setValue("implementer", ChangeRequest.implementer || "");
		}
	}, [ChangeRequest, setValue]);


	return (
		<div className="flex-wrap w-full">
			<NavBar />

			<div className="p-4 rounded-full">
				<div>
					<form
						onSubmit={handleSubmit(saveChangeRequest)}
						className="Form px-4">
						<div className="changeType flex items-center p-2">
							<label className="m-2">Change Type:</label>
							<select
								id="ChangeType"
								required
								{...register("changeType", { required: true })}
								className="bg-slate-200 border border-gray-300 text-gray-900 font-medium rounded-lg p-2"
								placeholder="Select Change Type">
								<option value="">Select Change Type</option>
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
								defaultValue={ChangeRequest.applicationId}
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

						<div className=" p-2">
							<div className="start flex items-center p-2">
								<label className="m-2">Scheduled Start Date/Time:</label>
								<input
									type="datetime-local"
									required
									{...register("timeWindowStart", { required: true })}
									className="flex-box m-2 block p-2 text-gray-900 border-2 border-gray-400 rounded-lg bg-gray-50 outline-none sm:text-md"
									value={dayjs(ChangeRequest.timeWindowStart).format(
										"YYYY-MM-DDTHH:mm"
									)}
								/>
							</div>
							<div className="end flex items-center p-2">
								<label className="m-2">Scheduled End Date/Time:</label>
								<input
									type="datetime-local"
									required
									{...register("timeWindowEnd", { required: true })}
									className="flex-box m-2 block p-2 text-gray-900 border-2 border-gray-400 rounded-lg bg-gray-50 outline-none sm:text-md"
									value={dayjs(ChangeRequest.timeWindowEnd).format(
										"YYYY-MM-DDTHH:mm"
									)}
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
									min="0"
								/>
							</div>
							<div className=" flex p-2">
								<fieldset className="flex items-center p-2 ">
									<label className="me-4">Risk Assessment:</label>
									<div className="flex items-center me-4">
										<input
											id="riskLow"
											className="peer riskLow"
											type="radio"
											name="risk"
											value="LOW"
											defaultChecked={ChangeRequest.riskLevel === "LOW"}
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
											defaultChecked={ChangeRequest.riskLevel === "MEDIUM"}
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
											defaultChecked={ChangeRequest.riskLevel === "HIGH"}
											type="radio"
											name="risk"
											value="HARD"
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
						<div className="flex flex-row justify-center mx-5">
							<div>
								<Link
									className="hover:border-black border-2 inline-block bg-gray-200 font-bold text-black p-2 rounded-lg m-2"
									to="/dashboard">
									Cancel
								</Link>

								<button
									className="hover:border-black border-2 bg-gray-200 font-bold text-black p-2 rounded-lg m-2"
									onClick={() => {
										denyChangeRequest()
									}}>
									Deny
								</button>

								<button
									type="submit"
									className="hover:border-black border-2 bg-gray-200 font-bold text-black p-2 rounded-lg m-2">
									Save
								</button>

								<button
									onClick={() => approveChangeRequest()}
									className="hover:border-black border-2 bg-gray-200 font-bold text-black p-2 rounded-lg m-2">
									Approve
								</button>
							</div>
						</div>
					</form>
				</div>
			</div>
		</div>
	)
}
export default GrabChangeRequest
