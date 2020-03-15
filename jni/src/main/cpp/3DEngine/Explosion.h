////
//// Created by jaccen on 2020-03-15.
////
//
//#ifndef ANDROIDLEARN_EXPLOSION_H
//#define ANDROIDLEARN_EXPLOSION_H
//
//#include "particle.h"
//
//
//const vec3      PARTICLE_VELOCITY  (0.0f, 2.0f, 0.0f);
//const vec3      VELOCITY_VARIATION  (4.0f, 4.0f, 4.0f);
//const vec3      PARTICLE_ACCELERATION  (0.0f, -5.0f, 0.0f);
//const float     PARTICLE_SIZE      = 3.0f;//5.0f;
//const float     SIZE_VARIATION     = 0.3f;//2.0f;
//#define FRAND   (((float)rand()-(float)rand())/RAND_MAX)
//
//class Explosion : public Particle{
//public:
//    Explosion(int maxParticles, vec3 origin, float spread, GLuint texture);
//    void  Update(float elapsedTime);
//    void  Render();
//    bool  IsDead() { return m_numParticles == 0; }
//
//    void    InitializeParticle(int index);
//    float   m_spread;
//    GLuint  m_texture;
//};
//
//
//#endif //ANDROIDLEARN_EXPLOSION_H
