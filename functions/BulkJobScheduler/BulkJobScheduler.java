
import java.util.logging.Level;
import java.util.logging.Logger;

import com.catalyst.Context;
import com.catalyst.job.JOB_STATUS;
import com.catalyst.job.JobRequest;
import com.catalyst.job.CatalystJobHandler;

import com.zc.common.ZCProject;
import com.zc.component.object.ZCObject;
import com.zc.component.object.ZCRowObject;

public class BulkJobScheduler implements CatalystJobHandler{

	private static final Logger LOGGER = Logger.getLogger(BulkJobScheduler.class.getName());

	@Override
	public JOB_STATUS handleJobExecute(JobRequest request, Context arg1) throws Exception {
		try {
			ZCProject.initProject();
			Object MODULE = request.getJobParam("MODULE");
			Object FIELDS_TO_BE_PROCESSED = request.getJobParam("FIELDS_TO_BE_PROCESSED");

			if (MODULE == null || MODULE.toString().isEmpty()) {
				throw new Exception("MODULE cannot be empty");
			}

			if (FIELDS_TO_BE_PROCESSED == null || FIELDS_TO_BE_PROCESSED.toString().isEmpty()) {
				throw new Exception("FIELDS_TO_BE_PROCESSED cannot be empty");
			}

			ZCRowObject row = ZCRowObject.getInstance();
			row.set("MODULE_NAME", MODULE.toString());
			row.set("FIELDS_TO_BE_PROCESSED", FIELDS_TO_BE_PROCESSED.toString().replaceAll("\\s", ""));
			ZCObject.getInstance().getTableInstance("BulkRead").insertRow(row);

			LOGGER.log(Level.SEVERE, "Inserted SucessFully:)");
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Exception in "+BulkJobScheduler.class.getName()+" Function ", e);
			return JOB_STATUS.FAILURE;
		}
		return JOB_STATUS.SUCCESS;
	}

}
