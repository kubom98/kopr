package cviko09.uloha1;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Phaser;

import javafx.application.Platform;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class SpellCheckerService extends Service<Boolean> {

	private StringProperty text;
	private Phaser needToCheckPhaser = new Phaser(1);

	public SpellCheckerService(StringProperty text) {
		this.text = text;
		text.addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				int phase = needToCheckPhaser.arrive();
				System.out.println("Skoncila faza: " + phase);
			}

		});
	}

	@Override
	protected Task<Boolean> createTask() {

		return new Task<Boolean>() {

			private String textValue;
			private SpellChecker spellChecker = new SpellChecker();

			@Override
			protected Boolean call() throws Exception {
				int unverifiedPhase = 0;
				while (true) {
					int phase = needToCheckPhaser.getPhase();
					if (unverifiedPhase == phase) {
						needToCheckPhaser.awaitAdvance(unverifiedPhase++);
						System.out.println("Faza " + unverifiedPhase + "skoncila.");
					} else {
						unverifiedPhase = phase;
					}
					CountDownLatch latch = new CountDownLatch(1);
					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							textValue = text.getValue();
							latch.countDown();
						}
					});
					latch.await();

					final List<SpellChecker.SpellcheckBoundary> kontrola = spellChecker.check(textValue);
					boolean isOK = kontrola.isEmpty();
					System.out.println(textValue + " kontrola ok: " + isOK);
					updateValue(isOK);
				}
			}

		};
	}

}
