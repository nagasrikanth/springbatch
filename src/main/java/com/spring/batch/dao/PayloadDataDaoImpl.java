package com.spring.batch.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.spring.batch.dto.AdditionalData;
import com.spring.batch.dto.RequestPayloadData;

@Repository
public class PayloadDataDaoImpl implements PayloadDataDao {
	@Autowired
	@Qualifier("oracleJdbcTemplate")
	private JdbcTemplate jdbcTemplate;

	@SuppressWarnings("unchecked")
	@Override
	public List<RequestPayloadData> getRequestPayloadData() {

		List<RequestPayloadData> querydat = null;
		List<RequestPayloadData> querydat1 = null;
		try {
			Object resutObject = getDat();
			querydat1 = (List<RequestPayloadData>) resutObject;
			querydat = (List<RequestPayloadData>) querydat1.get(0);
			System.out.println(querydat.size());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return querydat;
	}

	public List<Object> getDat() {
		String sql = "SELECT * FROM ROCM_DIGITAL_OPPTY rd LEFT JOIN  ROCM_DIGITAL_OPPTY_ADDL_DTL ad "
				+ "on rd.OPPTY_ID = ad.OPPTY_ID";

		return jdbcTemplate.query(sql,
                (rs, rowNum)  -> {
			Map<Long, RequestPayloadData> datamap = new HashMap<Long, RequestPayloadData>();
			while (rs.next()) {
				Long id = rs.getLong(1);
				RequestPayloadData data = datamap.get(id);
				if (data == null){
					data = new RequestPayloadData(rs.getLong(1),rs.getString(2),rs.getString(3),rs.getString(4)
							,new Date(rs.getTimestamp(5).getTime()),new Date(rs.getTimestamp(6).getTime())
							,rs.getLong(7),rs.getLong(8),rs.getLong(9),rs.getLong(10),rs.getString(11)
							,rs.getString(12),rs.getString(13),rs.getString(14),rs.getString(15),rs.getString(16)
							,rs.getString(17),rs.getString(18),rs.getString(19),rs.getLong(20),rs.getLong(21)
							,new Date(rs.getTimestamp(22).getTime()),new Date(rs.getTimestamp(23).getTime()),rs.getLong(24));
					datamap.put(id, data);
				}
				if (Optional.ofNullable(rs.getString("OPPTY_ID")).isPresent()) {
					List<AdditionalData> additionalList = data.prepareAdditionalData();
					if (additionalList == null) {
						additionalList = new ArrayList<>();
						data.setAdditionalData(additionalList);
					}
					AdditionalData addData = new AdditionalData(rs.getLong("OPPTY_ADDL_SEQ_ID"), rs.getLong("OPPTY_ID"),
							rs.getString("OPPTY_SRC_CD"), rs.getString("PGM_CD"), rs.getString("SUB_PGM_CD"),
							rs.getString("SGMNT_CD_LVL1"), rs.getString("SGMNT_CD_LVL2"), rs.getString("SGMNT_CD_LVL3"),
							rs.getString("SGMNT_CD_LVL4"), rs.getString("SGMNT_CD_LVL5"), rs.getString("SGMNT_CD_LVL6"),
							rs.getLong("SGMNT_SEQ_NBR"), rs.getString("ATRB_NM1"), rs.getString("ATRB_VAL1"),
							rs.getString("ATRB_NM2"), rs.getString("ATRB_VAL2"), rs.getString("ATRB_NM3"),
							rs.getString("ATRB_VAL3"), rs.getString("ATRB_NM4"), rs.getString("ATRB_VAL4"),
							rs.getString("ATRB_NM5"), rs.getString("ATRB_VAL5"), rs.getString("ATRB_NM6"),
							rs.getString("ATRB_VAL6"), rs.getString("ATRB_NM7"), rs.getString("ATRB_VAL7"),
							rs.getString("ATRB_NM8"), rs.getString("ATRB_VAL8"), rs.getString("ATRB_NM9"),
							rs.getString("ATRB_VAL9"), rs.getString("ATRB_NM10"), rs.getString("ATRB_VAL10"),
							rs.getLong("WKFLW_EXECN_ID"), new Date(rs.getTimestamp("WKFLW_EXECN_DT").getTime()),
							new Date(rs.getTimestamp("ODATE").getTime()), rs.getLong("SRC_WKFLW_EXECN_ID"));
					additionalList.add(addData);
				}
			}
			return  new ArrayList<>(datamap.values());
		});
	}

	/**
	 * @return the jdbcTemplate
	 */
	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	/**
	 * @param jdbcTemplate the jdbcTemplate to set
	 */
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
}
