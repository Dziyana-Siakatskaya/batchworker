package RestTemplateReader;

import java.util.List;

import org.springframework.batch.item.ItemWriter;

import com.epam.edu.student.model.Alerts;

public class RestTemplateWriter implements ItemWriter<Alerts>  {

	@Override
	public void write(List<? extends Alerts> items) throws Exception {
		for (Alerts temp : items) {
			System.out.println("PROCESSED " + temp.getJSON());
		}
	}

}
