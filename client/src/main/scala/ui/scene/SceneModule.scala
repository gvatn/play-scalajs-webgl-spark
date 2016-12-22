package ui.scene

trait SceneModule {
  def initSceneModule(scene: Scene): Unit = {}
  def sceneDraw(scene: Scene): Unit = {}
  def sceneItemDraw(scene: Scene, sceneItem: SceneItem): Unit = {}
}
