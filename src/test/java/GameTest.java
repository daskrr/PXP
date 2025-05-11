import processing.event.MouseEvent;
import pxp.engine.core.*;
import pxp.engine.core.component.*;
import pxp.engine.core.component.ui.*;
import pxp.engine.data.*;
import pxp.engine.data.assets.*;
import pxp.engine.data.event.PXPSingleEvent;
import pxp.engine.data.ui.Anchor;
import pxp.engine.data.ui.InteractableTransition;
import pxp.engine.data.ui.RenderMode;
import pxp.logging.Debug;

import java.util.ArrayList;

// Apologies for the messy code, this was used for testing everything :D

public class GameTest extends Game
{
    @Override
    public GameSettings startup() {
        AssetManager.createSprite("test", "test_img.png", 16);
        AssetManager.createSprite("long", "parallax_0.png", 16);
        AssetManager.createSpriteSheet("testSheet", "player.png", 16, 6, 15);
        AssetManager.createSound("testSound", "test_sound.wav");
        AssetManager.createSound("testSoundMono", "test_mono.wav");
//        AssetManager.createVideo("testVideo", "test_video.mov");
        AssetManager.createFont("fontTest", new FontAsset("Montserrat Regular", "Montserrat Bold", "Montserrat Italic", "Montserrat Bold Italic"));

        AssetManager.createSprite("fillRect", "slider_bg.png", 16);
        AssetManager.createSprite("knob", "knob.png", 16);

        return new GameSettings() {{
//            size = new Vector2(800,800);
            size = new Vector2(1280,720);
//            fullscreen = true;
//            resizable = true;
            targetFPS = 200;
            background = new Color(0,0,0,255);
            sortingLayers = new String[] {
                "Default",
                "Default2"
            };
        }};
    }

    @Override
    public Scene[] buildScenes() {
        return new Scene[] {
            new Scene(new GameObjectSupplier[] {
                // camera
                () -> new GameObject("cam", new Component[] {
                    new Camera(8f) {{
                        setFollowing("player");
                    }}
                }) {{
                    transform = new Transform(new Vector2(0,0));
                }},

                () -> new GameObject("canvas2", new Component[] { new Canvas() }, new GameObject[] {
                    new GameObject("sliderWrapper", new Component[] {
                        new Slider(InteractableTransition.COLOR, "fillRect", "handle") {{
                            setValue(0.25f);
                            setDirection(SliderDirection.LEFT_TO_RIGHT);
//                            wholeNumbers = true;
                            minValue = 0f;
                            maxValue = 10f;
                        }}
                    }, new GameObject[] {
                        new GameObject("background", new Component[] {
                            new Image(AssetManager.get("fillRect", SpriteAsset.class)) {{
                                color = new Color(100,100,100);
                            }}
                        }) {{
                            transform = new RectTransform(
                                new Vector2(0,0),
                                new Vector3(0,0,0),
                                new Vector2(1,1),
                                new Vector2(350, 50),
                                Anchor.CENTER
                            );
                        }},
                        new GameObject("fillRect", new Component[] {
                            new Image(AssetManager.get("fillRect", SpriteAsset.class))
                        }) {{
                            transform = new RectTransform(
                                new Vector2(0,0),
                                new Vector3(0,0,0),
                                new Vector2(1,1),
                                new Vector2(50, 50),
                                Anchor.CENTER
                            );
                        }},
                        new GameObject("handle", new Component[] {
                            new Image(AssetManager.get("knob", SpriteAsset.class))
                        }) {{
                            transform = new RectTransform(
                                new Vector2(0,0),
                                new Vector3(0,0,0),
                                new Vector2(1,1),
                                new Vector2(50, 50),
                                Anchor.CENTER
                            );
                        }}
                    }) {{
                        transform = new RectTransform(
                                new Vector2(0,0),
                                new Vector3(0,0,0),
                                new Vector2(1,1),
                                new Vector2(350, 50),
                                Anchor.CENTER
                        );
                    }}
                }),

                // testing
//                () -> new GameObject("test", new Component[] {
//                    new Canvas(new DrawEnvironment() {
//                        @Override
//                        public void accept(GameProcess p) {
//                            p.rectMode(CORNERS);
//                            p.rect(0,0,100,100);
//                        }
//                    }) {{
//                        sortingLayer = "Default";
//                        orderInLayer = 1;
//                    }},
//                    new SoundEmitter(AssetManager.get("testSoundMono", SoundAsset.class)) {{
//                        isSpatial = true;
//                        setAutoPlay(true);
//                        setLoop(true);
//                    }}
//                }) {{
//                    transform = new Transform(new Vector2(0,0), new Vector3(0,0,0), new Vector2(4,5));
//                }},
//                () -> new GameObject("test2", new Component[] {
//                    new SpriteRenderer(AssetManager.getSpriteFromSheet("testSheet", 65)) {{
//                        sortingLayer = "Default";
//                        orderInLayer = 0;
//                    }}
//                }) {{
//                    transform = new Transform(new Vector2(0,0), new Vector3(0,0,0), new Vector2(1,1));
//                }},
//                () -> new GameObject("test2", new Component[] {
//                    new SpriteRenderer(AssetManager.get("test", SpriteAsset.class)) {{
//                        sortingLayer = "Default";
//                        orderInLayer = 0;
//                        flipX = true;
//                        flipY = true;
//                    }}
//                }) {{
//                    transform = new Transform(new Vector2(0,0), new Vector3(0,0,0), new Vector2(1,1));
//                }},
                () -> {
                    Image image = new Image(AssetManager.get("test", SpriteAsset.class)) {{
                        preserveAspect = true;
                    }};

                    return new GameObject("Canvas", new Component[] { new Canvas(RenderMode.CAMERA) }, new GameObject[] {
                    new GameObject("textTest", new Component[] {
                        new Text("Text Test rfgehue It is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout. The point of using Lorem Ipsum is that it has a more-or-less normal distribution of letters, as opposed to using 'Content here, content here', making it look like readable English. Many desktop publishing packages and web page editors now use Lorem Ipsum as their default model text, and a search for 'lorem ipsum' will uncover many web sites still in their infancy. Various versions have evolved over the years, sometimes by accident, sometimes on purpose (injected humour and the like).") {{
                            pivot = Pivot.CENTER;
                            font = AssetManager.get("fontTest", FontAsset.class);
                            fontStyle = FontStyle.BOLD;
                        }}
                    }) {{
                        transform = new RectTransform(
                            new Vector2(0,0),
                            new Vector3(0,0,0),
                            new Vector2(1,1),
                            new Vector2(150, 150),
                            Anchor.TOP_CENTER
                        );
                    }},
                    new GameObject("testButton", new Component[] {
                        new Button(InteractableTransition.SPRITE_SWAP) {{
                            targetImage = image;//new Image(AssetManager.get("long", SpriteAsset.class)) {{
//                                preserveAspect = true;
//                            }};
                            hoverSprite = AssetManager.get("long", SpriteAsset.class);
                            onClick = new PXPSingleEvent<>() {
                                @Override
                                public void invoke(MouseEvent mouseEvent) {
                                    Debug.log("click");
                                }
                            };
                        }}
                    }, new GameObject[] {
                        new GameObject("testImage", new Component[] {
//                            new Image(AssetManager.get("test", SpriteAsset.class)) {{
////                                preserveAspect = true;
////                                pivot = Pivot.CENTER_LEFT;
//                            }}
                            image
                        }) {{
                            transform = new RectTransform(
                                new Vector2(0,0),
                                new Vector3(0,0,0),
                                new Vector2(1,1),
                                new Vector2(200, 200),
                                Anchor.CENTER
                            );
                        }}
                    }) {{
                        transform = new RectTransform(
                            new Vector2(250,0),
                            new Vector3(0,0,0),
                            new Vector2(1.5f,1),
                            new Vector2(200, 200),
                            Anchor.CENTER
                        );
                    }}
                }) {{
                    transform = new RectTransform();
                }};},
                () -> new GameObject("player", new Component[] {
                    new SpriteRenderer() {{
                        color = new Color(255,255,255, 100);
                        setSortingLayer("Default2");
                    }},
                    new Animation("idling", AssetManager.get("testSheet", SpriteAsset.class), 0, 5, 2f),
                    new Animation("walking", AssetManager.get("testSheet", SpriteAsset.class), 6, 13, 1f),
                    new Animation("climbing", AssetManager.get("testSheet", SpriteAsset.class), 58, 62, .7f),
                    new Animation("full", AssetManager.get("testSheet", SpriteAsset.class), 5f),
                    new Animator("idling"),
                    new ComponentTest(),
                    new SoundEmitter(AssetManager.get("testSoundMono", SoundAsset.class)) {{
                        isSpatial = true;
                        innerRadius = 10f;
                        setLoop(true);
                    }}
                }, new GameObject[] {
                    new GameObject("test2", new Component[] {
                        new SpriteRenderer(AssetManager.get("test", SpriteAsset.class)) {{
                            setSortingLayer("Default");
                            flipX = true;
                            flipY = true;
                        }}
                    }) {{
                        transform = new Transform(new Vector2(4,4), new Vector3(0,0,0), new Vector2(1,1));
                    }},
                }) {{
                    transform = new Transform(new Vector2(3,3), new Vector3(0,0,0), new Vector2(1,1));
                }}
            }),
            new Scene(new GameObjectSupplier[]{
                // camera
                () -> new GameObject("cam", new Component[] {
                    new Camera(8f) {{
                        setFollowing("player");
                    }}
                }) {{
                    transform = new Transform(new Vector2(0, 0));
                }},
//                () -> new GameObject("player", new Component[] {
//                        new SpriteRenderer() {{
//                            setSortingLayer("Default2");
//                        }},
//                        new Animation("idling", AssetManager.get("testSheet", SpriteAsset.class), 0, 5, 2f),
//                        new Animation("walking", AssetManager.get("testSheet", SpriteAsset.class), 6, 13, 1f),
//                        new Animation("climbing", AssetManager.get("testSheet", SpriteAsset.class), 58, 62, .7f),
//                        new Animation("full", AssetManager.get("testSheet", SpriteAsset.class), 5f),
//                        new Animator("idling"),
//                        new ComponentTest(),
//                        new SoundEmitter(AssetManager.get("testSoundMono", SoundAsset.class)) {{
//                            isSpatial = true;
//                            innerRadius = 10f;
//                            setLoop(true);
//                        }}
//                }),
            })
        };
    }

    public static void main(String[] args) {
        new GameTest();
    }
}
