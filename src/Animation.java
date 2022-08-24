public class Animation {
    public int currentFrame = 0;
    public int fps;
    private int frameCount;
    private long previousTimeCheck;
    public Vector2 position;
    public Vector2 size;
    public Vector2 textureCoords;
    public Vector2 frameSize;
    private Vector2 framePosition;
    public Texture texture;
    Animation(Texture texture, float xPosition, float yPosition, float width, 
        float height, float xTextureCoord, float yTextureCoord, 
        float frameWidth, float frameHeight, int frameCount, int fps) 
    {
        position = new Vector2(xPosition, yPosition);
        size = new Vector2(width, height);
        textureCoords = new Vector2(xTextureCoord, yTextureCoord);
        framePosition = new Vector2(xTextureCoord, yTextureCoord);
        frameSize = new Vector2(frameWidth, frameHeight);
        this.frameCount = frameCount;
        this.fps = fps;
        this.texture = texture;
        previousTimeCheck = System.currentTimeMillis();
    }
    public void addFrameToRenderer(Renderer renderer) {
        renderer.addQuad(position.x, position.y, size.x, size.y,
            framePosition.x, framePosition.y, frameSize.x, frameSize.y, texture);
    }
    public void setFrame(int frame) {
        currentFrame = frame;
        framePosition.x = frameSize.x * frame + textureCoords.x;
    }
    public void update() {
        if (System.currentTimeMillis() - previousTimeCheck >= (1000 / fps)) {
            if (currentFrame < frameCount - 1) {
                currentFrame += 1;
                setFrame(currentFrame);
                previousTimeCheck = System.currentTimeMillis();
            }
            else {
                currentFrame = 0;
                setFrame(currentFrame);
                previousTimeCheck = System.currentTimeMillis();
            }
        }
    }
}
