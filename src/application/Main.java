package application;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application {
	private static final int PASSWORD_LENGTH_LIMIT = 20, NAME_LENGTH_LIMIT = 20;
	private static final File ACCOUNTS_FILE = new File("C:/javaWork/workspace/LoginForm/src/application/files/accounts.txt");

	private Scene scene;
	private StackPane loginPane, registerPane, loggedInPane;
	private Button login, register, cancel, rOk;
	private TextField email, rEmail, rName;
	private PasswordField password, rPassword, rPassword1;
	private Timeline timeline;
	private Label loggedInLabel, stateLabel, rStateLabel;

	private List<Account> accounts;
	private String name;

	public Main() {
		loginPane = new StackPane();
		registerPane = new StackPane();
		loggedInPane = new StackPane();
		login = new Button("로그인");
		register = new Button("회원가입");
		cancel = new Button("취소");
		email = new TextField();
		rOk = new Button("회원가입");
		password = new PasswordField();
		rEmail = new TextField();
		rName = new TextField();
		rPassword = new PasswordField();
		rPassword1 = new PasswordField();
		scene = new Scene(loginPane, 500, 350);
		loggedInLabel = new Label();
		timeline = new Timeline();
		accounts = new ArrayList<>();
		stateLabel = new Label("");
		rStateLabel = new Label("");

		accounts = new ArrayList<>();

		getAccounts();
	}

	@Override
	public void start(Stage stage) {
		Label label0 = new Label("로그인"), label1 = new Label("이메일"), label2 = new Label("비밀번호"), label3 = new Label("회원가입"), label4 = new Label("이메일"),
			label5 = new Label("비밀번호"), label6 = new Label("비밀번호 확인"), label7 = new Label("이름(닉네임)");

		TextField[] textFields = new TextField[] { email, password, rEmail, rPassword, rPassword1, rName };

		for (TextField tf : textFields) {
			tf.setPrefWidth(250);
		}

		// 로그인 UI
		HBox hbox0 = new HBox(), hbox1 = new HBox(), hbox2 = new HBox();
		hbox0.getChildren().addAll(label1, email);
		hbox1.getChildren().addAll(label2, password);
		hbox2.getChildren().addAll(login, register);
		VBox vbox0 = new VBox();
		vbox0.getChildren().addAll(label0, hbox0, hbox1, hbox2, stateLabel);
		vbox0.setSpacing(10);
		vbox0.setAlignment(Pos.CENTER);

		// 회원가입 UI
		HBox hbox00 = new HBox(), hbox01 = new HBox(), hbox02 = new HBox(), hbox03 = new HBox(), hbox04 = new HBox();
		hbox00.getChildren().addAll(label4, rEmail);
		hbox01.getChildren().addAll(label5, rPassword);
		hbox02.getChildren().addAll(label6, rPassword1);
		hbox03.getChildren().addAll(label7, rName);
		hbox04.getChildren().addAll(rOk, cancel);
		VBox vbox00 = new VBox();
		vbox00.getChildren().addAll(label3, hbox00, hbox01, hbox02, hbox03, hbox04, rStateLabel);
		vbox00.setSpacing(10);
		vbox00.setAlignment(Pos.CENTER);

		HBox[] hboxes = new HBox[] { hbox0, hbox1, hbox2, hbox00, hbox01, hbox02, hbox03, hbox04 };

		for (HBox hbox : hboxes) {
			hbox.setAlignment(Pos.CENTER);
			hbox.setSpacing(3);
		}

		loginPane.getChildren().addAll(vbox0);
		StackPane.setAlignment(vbox0, Pos.CENTER);

		registerPane.getChildren().addAll(vbox00);
		StackPane.setAlignment(vbox00, Pos.CENTER);

		loggedInPane.getChildren().add(loggedInLabel);
		StackPane.setAlignment(loggedInLabel, Pos.CENTER);

		loginPane.setStyle("-fx-background-color: skyblue;");
		registerPane.setStyle("-fx-background-color: white;");
		loggedInPane.setStyle("-fx-background-color: lightblue;");

		loggedInLabel.setStyle("-fx-font-size: 20pt;");
		label0.setStyle("-fx-font-size: 16pt;");
		label3.setStyle("-fx-font-size: 16pt;");
		stateLabel.setStyle("-fx-text-fill: red;");
		rStateLabel.setStyle("-fx-text-fill: red;");

		// 화면 전환 애니매이션
		register.setOnAction(e -> {
			stateLabel.setText("");
			registerPane.translateXProperty().set(scene.getWidth());
			loginPane.getChildren().add(registerPane);

			KeyValue kv = new KeyValue(registerPane.translateXProperty(), 0, Interpolator.SPLINE(1, 0, 0, 1));
			KeyFrame kf = new KeyFrame(Duration.seconds(1), kv);

			timeline.getKeyFrames().clear();
			timeline.getKeyFrames().add(kf);
			timeline.setOnFinished(e1 -> {
				loginPane.getChildren().remove(registerPane);
				scene.setRoot(registerPane);
			});
			timeline.play();
		});
		cancel.setOnAction(e -> {
			rStateLabel.setText("");
			loginPane.translateXProperty().set(-scene.getWidth());
			registerPane.getChildren().add(loginPane);

			KeyValue kv = new KeyValue(loginPane.translateXProperty(), 0, Interpolator.SPLINE(1, 0, 0, 1));
			KeyFrame kf = new KeyFrame(Duration.seconds(1), kv);

			timeline.getKeyFrames().clear();
			timeline.getKeyFrames().add(kf);
			timeline.setOnFinished(e1 -> {
				registerPane.getChildren().remove(loginPane);
				scene.setRoot(loginPane);
			});
			timeline.play();
		});
		rOk.setOnAction(e -> {
			if (register(rEmail.getText().trim(), rPassword.getText().trim(), rPassword1.getText().trim(), rName.getText().trim())) {
				rStateLabel.setText("");
				loginPane.translateXProperty().set(-scene.getWidth());
				registerPane.getChildren().add(loginPane);

				KeyValue kv = new KeyValue(loginPane.translateXProperty(), 0, Interpolator.SPLINE(1, 0, 0, 1));
				KeyFrame kf = new KeyFrame(Duration.seconds(1), kv);

				timeline.getKeyFrames().clear();
				timeline.getKeyFrames().add(kf);
				timeline.setOnFinished(e1 -> {
					registerPane.getChildren().remove(loginPane);
					scene.setRoot(loginPane);
				});
				timeline.play();
			}
		});
		login.setOnAction(e -> {
			if (login(email.getText().trim(), password.getText().trim())) {
				loggedInLabel.setText(name + " 님 환영합니다.");
				stateLabel.setText("");
				loggedInPane.translateYProperty().set(-scene.getHeight());
				loginPane.getChildren().add(loggedInPane);

				KeyValue kv = new KeyValue(loggedInPane.translateYProperty(), 0, Interpolator.SPLINE(1, 0, 0, 1));
				KeyFrame kf = new KeyFrame(Duration.seconds(1), kv);

				timeline.getKeyFrames().clear();
				timeline.getKeyFrames().add(kf);
				timeline.setOnFinished(e1 -> {
					loginPane.getChildren().remove(loggedInPane);
					scene.setRoot(loggedInPane);
				});
				timeline.play();
			}
		});
		rName.setOnKeyPressed(e -> {
			if (rName.getText().length() >= NAME_LENGTH_LIMIT) {
				rStateLabel.setText("이름은 20자까지만 입력 가능합니다.");
				rName.setText(rPassword1.getText().substring(0, 19));
			} else {
				rStateLabel.setText("");
			}
		});
		rPassword.setOnKeyPressed(e -> {
			if (rPassword.getText().length() >= PASSWORD_LENGTH_LIMIT) {
				rStateLabel.setText("비밀번호는 20자까지만 입력 가능합니다.");
				rPassword.setText(rPassword.getText().substring(0, 19));
			} else {
				rStateLabel.setText("");
			}
		});
		rPassword1.setOnKeyPressed(e -> {
			if (rPassword.getText().length() >= PASSWORD_LENGTH_LIMIT) {
				rStateLabel.setText("비밀번호는 20자까지만 입력 가능합니다.");
				rPassword1.setText(rPassword1.getText().substring(0, 19));
			} else {
				rStateLabel.setText("");
			}
		});
		password.setOnKeyPressed(e -> {
			if (password.getText().length() >= PASSWORD_LENGTH_LIMIT) {
				stateLabel.setText("비밀번호는 20자까지만 입력 가능합니다.");
				password.setText(rPassword1.getText().substring(0, 19));
			} else {
				stateLabel.setText("");
			}
		});

		stage.setScene(scene);
		stage.setResizable(false);
		stage.setTitle("JavaFX - Login Form");
		stage.show();
	}

	private boolean login(String email, String password) {
		if (email == null || password == null || email.length() == 0 || password.length() == 0) {
			stateLabel.setText("빈칸을 모두 채워주세요.");
			return false;
		}

		if (!Pattern.matches("^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$", email)) {
			stateLabel.setText("이메일 형식이 아닙니다.");
			return false;
		}

		Account foundAccount = null;

		for (Account account : accounts) {
			if (account.getEmail().equals(email)) {
				foundAccount = account;
			}
		}

		if (foundAccount != null) {
			stateLabel.setText("");

			if (!foundAccount.getPassword().equals(password)) {
				stateLabel.setText("비밀번호가 일치하지 않습니다.");
				return false;
			} else {
				name = foundAccount.getName();
			}
		} else {
			stateLabel.setText("존재하지 않는 계정입니다.");
			return false;
		}

		return true;
	}

	private boolean register(String email, String password, String password1, String name) {
		if (!password.equals(password1)) {
			rStateLabel.setText("비밀번호가 일치하지 않습니다.");
			return false;
		}

		if (!Pattern.matches("^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$", email)) {
			rStateLabel.setText("이메일 형식이 아닙니다.");
			return false;
		}

		if (email == null || password == null || password1 == null || name == null || email.length() == 0 || password.length() == 0
			|| password1.length() == 0 || name.length() == 0) {
			stateLabel.setText("빈칸을 모두 채워주세요.");
			return false;
		}

		for (Account a : accounts) {
			if (a.getName().equals(name)) {
				rStateLabel.setText("이미 사용된 이름입니다.");
				return false;
			} else if (a.getEmail().equals(email)) {
				rStateLabel.setText("이미 사용된 이메일입니다.");
				return false;
			}
		}

		email.trim();
		password.trim();
		name.trim();

		try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(ACCOUNTS_FILE, true), Charset.forName("UTF-8")))) {
			String accountData = email + "/" + password + "/" + name;
			writer.write(accountData);
			writer.newLine();
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		Account account = new Account(email, password, name);
		accounts.add(account);

		return true;
	}

	private void getAccounts() {
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(ACCOUNTS_FILE), Charset.forName("UTF-8")))) {
			String read = "";

			while ((read = reader.readLine()) != null) {
				StringTokenizer st = new StringTokenizer(read, "/");
				String email = st.nextToken(), password = st.nextToken(), name = st.nextToken();

				Account account = new Account(email, password, name);
				System.out.println(email + ", " + password + ", " + name);
				accounts.add(account);
			}
			
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
 
	public static void main(String[] args) {
		launch(args);
	}
}
