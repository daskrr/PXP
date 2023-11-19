# PXP
Processing Experience | Game Engine made in Processing for Saxion UAS | CMGT

## Usage

### Main class

This is where it all begins. Setting up some general game settings, and pointing to the contents of the game.
```java
public class MyGame extends Game
{
    @Override
    public GameSettings startup() {
        // adding assets to the game
        AssetManager.createSprite("myAwesomeImage", "image.png", 16);
        // the asset path doesn't need to contain 'data/', but the
        // asset needs to be placed in a data directory

        // needs to return the game settings
        return new GameSettings() {{
            size = new Vector2(1280,720);
            fullscreen = true;
            
            targetFPS = 140;
            background = new Color(0,0,0,255);
            
            // the sorting layers need to contain "Default"
            // if they don't the "Default" layer will be placed automatically
            // at the 0th index
            sortingLayers = new String[] {
                "Default",
                "Objects",
                "Enemies",
                "Player",
            };
            // the layers are used for collisions
            // all layers collide with one another
            layers = new String[] {
                "Default",
                "Objects",
                "Entities"
            };
            // here layers that SHOULDN'T collide are mentioned in pairs
            ignoreCollisionLayers = new ArrayList<>() {{
                add(new Pair<>("Default", "Objects"));
            }};
        }};
    }

    @Override
    public Scene[] buildScenes() {
        // here ALL the scenes of the game are created
        // they can be created locally, using functional programming, but this can
        // get messy easily.
        
        // so, the suggested method is creating scenes individually as super classes,
        // then instantiating them here.
        return new Scene[] {
            new MainMenu(),
            new Level1(),
            new Level2(),
            new BossFight()
        };
    }

    // we need to start the game somehow, right?
    public static void main(String[] args) {
        new MyGame();
    }
}
```

### Scenes
The scenes of the game are very important. There can be as many as needed,
and they hold all game objects with their functionality.

All scenes are required to contain a camera!
```java
class Level1 extends Scene
{
    public Level1() {
        super();

        // now we use the setGameObjects method to create the scene and its game objects
        // we need to provide a game object supplier array containing all game objects
        GameObjectSupplier[] suppliers = new GameObjectSupplier[] {
            // all scenes must have a camera
            () -> new GameObject("mainCamera", new Component[] {
                new Camera(8f) {{ // 8f is the ortho size (half of the screen height)
                    setFollowing("player"); 
                    // this forces the camera to snap to the player's transform position
                }}
            }) 
            // the camera can have a position attached to it too
            // {{
            //     transform = new Transform(new Vector2(5, 1));
            // }}
            ,
                
            // this is a game object made "on the fly"
            () -> new GameObject("tree", new Component[] {
                new SpriteRenderer(AssetManager.get("myAwesomeImage", SpriteAsset.class))
            }) {{
                // we set the position using an instance initializer
                transform = new Transform(new Vector2(4,0));
            }},
            // these could be regarded to as what Unity would call a "prefab"
            // we'll take a look at them below
            () -> new Player() {{
                transform = new Transform(new Vector2(1,0));
            }},
            () -> new Enemy() {{
                transform = new Transform(new Vector2(4,0));
            }}
        };
        
        // finally, we set the objects
        this.setGameObjects(suppliers);
    }
}
```

### Prefabs
"Prefabs" help with avoiding code repetition and ease of use. Whether it's just a tree, rock or it's an entire 
room with a boss fight, this way of storing game objects and their functionality is crucial for a productive environment.   
```java
class Player extends GameObject
{
    public Player() {
        // creating the player game object can easily be done like this
        super(
            "player",
            new Component[] {
                // here we set up an empty sprite renderer, as the animator will handle what it displays
                new SpriteRenderer() {{
                    color = new Color(255,255,255, 100);
                    setSortingLayer("Player");
                }},
                // then we define the possible animations
                new Animation("idling", AssetManager.get("player", SpriteAsset.class), 0, 5, 2f),
                new Animation("walking", AssetManager.get("player", SpriteAsset.class), 6, 13, 1f),
                new Animation("climbing", AssetManager.get("player", SpriteAsset.class), 58, 62, .7f),
                new Animation("full", AssetManager.get("player", SpriteAsset.class), 5f),
                // finally, setting up the animator, giving it the default animation
                new Animator("idling"),
                
                // we configure a sound emitter for the player
                // this also shows usage of "3D sound", but it shouldn't make a difference if the camera follows the player    
                new SoundEmitter(AssetManager.get("playerWalk", SoundAsset.class)) {{
                    isSpatial = true;
                    innerRadius = 10f;
                    setLoop(true);
                }},
    
                // this is an example of using a custom component
                new PlayerController(),
            },
            new GameObject[] {
                // ... children can be placed here
            }
        );
    }
}
```

### Custom Components
Finally, custom components are a must for creating behaviours and, well, adding functionality to your game.

Heavily inspired by Unity's ``MonoBehaviours``, the ``Component`` provides useful methods to implement custom functionality.  
```java
public class PlayerController extends Component
{
    public PlayerController() {
        // the constructor can be left blank or take parameters, it's up to the developer
    }
    
    @Override
    public void awake() {
        // executes when the component is assigned to the game object
        // this should not be used to communicate outside this component's scope
        // ...
    }
    
    @Override
    public void start() {
        // executes when the scene is loaded
        // this can be used to communicate with other components or game objects
        // ...
    }

    @Override
    public void update() {
        // executes every frame
        // ...
    }

    @Override
    public void destroy() {
        // executes when the game object or component is destroyed or when the scene changes
        // ...
    }
    
    @Override
    public void gizmosDraw() {
        // used to draw on the screen for development purposes
        // this uses local space coordinates
        // use ctx() to gain access to Processing
    }
    
    // the following are called for all components of the game object, if and when
    // the game object has either a collider or a trigger collider attached to it
    public void collisionEnter(Collision collision) { }
    public void collisionStay(Collision collision) { }
    public void collisionExit(Collision collision) { }
    public void triggerEnter(Collider collider) { }
    public void triggerStay(Collider collider) { }
    public void triggerExit(Collider collider) { }
}
```

This was just a short how-to for creating a game using the PXP engine. For an extensive documentation
go to: https://cmgt.alexvasile.net/pxp/

There lie the dreaded JavaDocs. Good luck!
