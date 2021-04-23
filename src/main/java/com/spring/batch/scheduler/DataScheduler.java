package com.spring.batch.scheduler;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import com.spring.batch.dto.AdditionalData;
import com.spring.batch.dto.RequestPayloadData;
import com.spring.batch.services.DataServices;

@EnableScheduling
@EnableAsync
@Component
public class DataScheduler {

	@Autowired
	ThreadPoolTaskExecutor executors;
	@Autowired
	DataConig config;
	@Autowired
	DataServices dataServices;
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss:SSS");

	private static final Logger LOGGER = LoggerFactory.getLogger(DataScheduler.class);

	@Scheduled(fixedRateString = "#{@config.getFixedRate()}", initialDelayString = "#{@config.getInitialDelay()}")
	public void execute() {
		try {
			System.out.println("The time is now  START: {} " + dateFormat.format(new Date()));
			LOGGER.debug("The time is now  START: {} ", dateFormat.format(new Date()));
			List<RequestPayloadData> serialData = dataServices.getRequestPayloadData();
			serialData.forEach(data -> {
				try {
					String datakv = this.prepareDataJsonObject(data);
					System.out.println("JSON STRING " + datakv);
					LOGGER.info("JSON STRING :", datakv);
					datakv = null;
				} catch (Exception e) {
					e.printStackTrace();
				}
			});

		} catch (Exception e) {
			LOGGER.debug("Exception while running Scheduler", e);
		} finally {
			System.out.println("The time is now  END: {} " + dateFormat.format(new Date()));
			LOGGER.debug("The time is now  END: {} ", dateFormat.format(new Date()));
			LOGGER.debug("ThreadPoolScheduler is finally called after Scheduled Event");
		}
	}

	private String prepareDataJsonObject(RequestPayloadData data) {
		JSONObject json = new JSONObject();
		try {
			JSONObject json1 = new JSONObject();
			json1.put("appName", "CRM-DIGITAL-PNP");
			json1.put("lineOfBusiness", "Retail");
			json1.put("conversationID", "Id-e314fac206094fd9bc2bf64b39d072d7");
			json.put("requestMetaData", json1);

			JSONObject json2 = new JSONObject();
			json2.put("id", data.getId());
			json2.put("opptyActionCode", data.getOpptyActionCode());
			json2.put("pgmcd", data.getPgmcd());
			json2.put("subpgmcd", data.getSubpgmcd());
			json2.put("deliveryDT", data.getDeliveryDT());
			json2.put("ooptyExpnDT", data.getOoptyExpnDT());
			json2.put("rcomCycleNBR", data.getRcomCycleNBR());
			json2.put("opptyStatusCD", data.getOpptyStatusCD());
			json2.put("opptySubStatusCD", data.getOpptySubStatusCD());
			json2.put("prtyRank", data.getOoptyPrtyRank());
			json2.put("memberType", data.getMemberType());
			json2.put("memberID", data.getMemberID());
			json2.put("campaignId", data.getCampaignId());
			json2.put("eventName", data.getEventName());
			json2.put("subCampaignType", data.getSubCampaignType());
			json2.put("channelType", data.getChannelType());
			json2.put("channelID", data.getChannelID());
			json2.put("emailSubject", data.getEmailSubject());
			json2.put("emailPreview", data.getEmailPreview());
			json2.put("srcCycleNBR", data.getSrcCycleNBR());
			json2.put("wkflwExecnID", data.getWkflwExecnID());
			json2.put("wkflwExecnDT", data.getWkflwExecnDT());
			json2.put("opptyStatus", data.getOpptyStatus());
			json2.put("opptySubStatus", data.getOpptySubStatus());
			json2.put("oDate", data.getODate());
			json2.put("srcWkflwExecnID", data.getSrcWkflwExecnID());
			json.put("requestPayloadData", json2);

			List<AdditionalData> adilist = data.prepareAdditionalData();

			prepareJsonForAddList(adilist, json);
			adilist.clear();

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return json.toString();
	}

	private void prepareJsonForAddList(List<AdditionalData> adilist, JSONObject json) {
		JSONArray facilityInfoArray = new JSONArray();
		JSONArray prescriberInfoArray = new JSONArray();
		JSONArray messageContentInfoArray = new JSONArray();
		JSONArray scriptInfoArray = new JSONArray();
		try {
			for (AdditionalData additionalData : adilist) {
				String sgmntCdLVL2 = additionalData.getSgmntCdLVL2();
				if(sgmntCdLVL2 == null){
					continue;
				}
				switch (sgmntCdLVL2) {
				case "patientInfo":
					setPatientInfo(additionalData, json);
					break;
				case "facilityInfo":
					prepareJsonArrayForInfoData(additionalData, facilityInfoArray);
					break;
				case "prescriberInfo":
					prepareJsonArrayForInfoData(additionalData, prescriberInfoArray);
					break;
				case "messageContentInfo":
					prepareJsonArrayForInfoData(additionalData, messageContentInfoArray);
					break;
				case "scriptInfo":
					prepareJsonArrayForInfoData(additionalData, scriptInfoArray);
					break;
				default:
					LOGGER.info("No Additional Data are available");
				}
			}

			json.put("facilityInfo", facilityInfoArray);
			json.put("prescriberInfo", prescriberInfoArray);
			json.put("messageContentInfo", messageContentInfoArray);
			json.put("scriptInfo", scriptInfoArray);
			facilityInfoArray = null;
			prescriberInfoArray = null;
			messageContentInfoArray = null;
			scriptInfoArray =null;
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void prepareJsonArrayForInfoData(AdditionalData additionalData, JSONArray jsonArray) {
		try {
			JSONObject jsonKeyValue = getJsonKeyValue(additionalData);
			jsonArray.put(jsonKeyValue);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void setPatientInfo(AdditionalData additionalData, JSONObject json) {
		try {
			JSONObject jsonKeyValue = getJsonKeyValue(additionalData);
			json.put("patientInfo", jsonKeyValue);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private JSONObject getJsonKeyValue(AdditionalData additionalData) throws JSONException {
		JSONObject json = new JSONObject();
		json.put(additionalData.getAtrbNM1(), additionalData.getAtrbVAL1());
		json.put(additionalData.getAtrbNM2(), additionalData.getAtrbVAL2());
		json.put(additionalData.getAtrbNM3(), additionalData.getAtrbVAL3());
		json.put(additionalData.getAtrbNM4(), additionalData.getAtrbVAL4());
		json.put(additionalData.getAtrbNM5(), additionalData.getAtrbVAL5());
		json.put(additionalData.getAtrbNM6(), additionalData.getAtrbVAL6());
		json.put(additionalData.getAtrbNM7(), additionalData.getAtrbVAL7());
		json.put(additionalData.getAtrbNM8(), additionalData.getAtrbVAL8());
		json.put(additionalData.getAtrbNM9(), additionalData.getAtrbVAL9());
		json.put(additionalData.getAtrbNM10(), additionalData.getAtrbVAL10());

		return json;
	}
}
