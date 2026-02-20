public class Main extends Application {
	@Override
	public void start(Stage scoreboradSettings) {
		  //Make parent for both scenes
	      Parent root = null;
	      Parent root2 = null;
	      FXMLLoader loader = null;
	      FXMLLoader loader2 = null;

          //loads the module 
	      //ScoreBoardSettingModule model = new ScoreBoardSettingModule(0, 0);

	      //Try to load both scenes  
	      try{
	    	 loader = new FXMLLoader(getClass().getResource("testscenebulider.fxml"));
	    	 //loader2 = new FXMLLoader(getClass().getResource("/application/View/ScoreboardVisualScene.fxml"))
	    	 root = loader.load();
		     //root2 = loader2.load();
	      }
	      catch (Exception e){
	         e.printStackTrace();
	      } 
	      //Ensure that both controllers are using the same model
	      //ScoreboardSettingsController settingsController = loader.getController();
	      //settingsController.setModel(model);
	      //ScoreboardVisualController visualController = loader2.getController();
	      //visualController.setModel(model);
	      //Set the stage with the proper scene 
	      Scene scoreboradSettingsScene = new Scene(root);
	      scoreboradSettings.setTitle("ScoreBoard Setting");
	      scoreboradSettings.setScene(scoreboradSettingsScene);
	      scoreboradSettings.show();
	      
	    //   Stage scoreboardVisual = new Stage();
	    //   Scene scoreboradVisualScene = new Scene(root2);
	    //   scoreboardVisual.setTitle("ScoreBoard Visual");
	    //   scoreboardVisual.setScene(scoreboradVisualScene);
	    //   scoreboardVisual.show();

	}
	
	public static void main(String[] args) {
		launch(args);
	}
}